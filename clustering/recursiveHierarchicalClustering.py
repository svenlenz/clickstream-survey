#!/usr/bin/python

# the script to recursively run divisive hierchical clustering
# stopping each time at sweet spot
# for each iteration, exclude the defining features for each cluster

# arguments

# arg1: path to a file containing the different ngram count mapping
# example:
# 10 \t A5A(1)y8y(29)...
# the first col is line number
# the second col is a list of pattern(count)

# arg2: path to a directory to put the output file

# arg3: sizeThreshold, defines the minimum size of the cluster, that we are going to further divide
#	0.05: means the size cannot be less than 5% of the total instances

# result:

# resultDir/matrix.dat: store the pairwise distance matrix
#  if the file exist before the scirpt is run, the script assume the matrix
#  is correct and will read it in
# resultDir/result.json: store the clustering result
#  the result is recorded in a nested list, in the form of
# [type, list of sub-clusters, information about the cluster]

import numpy as np
from array import *
import sys, os, calculateDistance, math, time, json, mutual_info

# parse the sid ngram map
def getSidNgramMap(inputPath, sids = None):
	sid_seq = {}

	for line in open(inputPath):
		# get the sid
		sid = int(line.split('\t')[0])
		if sids and not sid in sids:
			continue
		# get the ngram
		line = line.strip()
		line = line.split('\t')[1]
		# remove the trailing ) so that the spliting would not have an empty tail
		line = line[:-1].split(')')
		sid_seq[sid] = dict([(item[0], int(item[1])) for item in map(lambda x: x.split('('), line)])
	return sid_seq

# baseCluster: take in the cluster to be splited
# baseSum: the distance from each node to other nodes in the same cluster, used to save computation
# matrix: distance matrix
def splitCluster((baseCluster, diameter, baseSum, cid), matrix):
	newCluster = []
	baseNodes = totalNodes = len(baseCluster)	# the number of nodes in the base cluster
	sumDists = []	# store the sum of distance to the base cluster
	newDists = []	# store the sum of distance to the new cluster

	if (baseSum):
		sumDists = baseSum
		newDists = [0 for idx in range(totalNodes)]
	else:
		for node in baseCluster:
			sumDist = 0
			distRow = matrix[node]
			for nodeT in baseCluster:
				sumDist += distRow[nodeT]
			sumDists.append(sumDist)
			newDists.append(0)

	while baseNodes > 1:
		# we want to calculate sum(A)/len(A) - sum(B)/len(B) where A is base and B is new
		# so we instead calculate sum(A) * len(B) - sum(B) * len(A) = (sum(T) - sum(B)) * len(B) - sum(B) * len(A)
		# note that baseNodes = len(A) + 1
		newNodes = totalNodes - baseNodes
		if baseNodes == totalNodes:
			difDist = sumDists
			maxIdx = np.argmax(difDist)
			maxValue = difDist[maxIdx]
		else:
			maxValue = 0
			for idx in range(len(sumDists)):
				sumT = sumDists[idx]
				sumB = newDists[idx]
				diff = (sumT-sumB) * newNodes - sumB * (baseNodes - 1)
				if diff > maxValue:
					maxValue = diff
					maxIdx = idx
			 # [(sumT-sumB) * newNodes - sumB * (baseNodes - 1) for (sumT, sumB) in zip(sumDists, newDists)]
		# print(difDist, sumDists, newDists)
		# find the node furthest away from the current cluster
		# maxIdx = np.argmax(difDist)
		# if (difDist[maxIdx] <= 0):
		if (maxValue <= 0):
			break
		newCluster.append(baseCluster[maxIdx])

		# update the distance to new cluster for all nodes not in the cluster
		distRow = matrix[baseCluster[maxIdx]]
		newDists = [newDists[idx] + distRow[baseCluster[idx]] for idx in range(baseNodes) if not idx == maxIdx]
		baseNodes -= 1
		del sumDists[maxIdx]
		del baseCluster[maxIdx]

	baseSum = [sumT - sumB for (sumT, sumB) in zip(sumDists, newDists)]
	return(baseCluster, newCluster, baseSum)

# given a matrix, possibly only part for the origianl matrix
# return the maxDistance
def getDia(cluster, matrix):
	if (not matrix):
		return 0
	# cluster = [sidToIdx[idx] for idx in cluster]
	maxDis = 0
	for node in cluster:
		sumDist = 0
		distRow = matrix[node]
		for nodeT in cluster:
			if (maxDis < distRow[nodeT]):
				maxDis = distRow[nodeT]
	return maxDis


# since we are going to use the tfidf frequency as the actual
# weight on each feature vector
# we need to calculate idf based on the users in the parent cluster
# and original frequency
def getIdf(sid_seq, sids):
	# sids = [x + 1 for x in sids]
	# print('in get idf')
	# print(sids)
	feqMap = {}
	total = len(sids)
	for sid in sids:
		stotal = sum([x[1] for x in sid_seq[sid].items()])
		for word in sid_seq[sid]:
			freq = sid_seq[sid][word]
			try:
				feqMap[word] += float(freq) / stotal
			except:
				feqMap[word] = float(freq) / stotal
	for word in feqMap:
		feqMap[word] = math.log(float(total) / feqMap[word])
	return feqMap


# the L-method is used to find out the sweetspot for defining features
# it is supposed to run iteratively until the sweetspot does not gets closer to 0
# assuming the input is a list of (x,y)
# ref: http://ieeexplore.ieee.org/xpls/abs_all.jsp?arnumber=1374239&tag=1
def getSweetSpotL(evalResults):
	if len(evalResults) == 0: return 0
	cutoff = currentKnee = evalResults[-1][0]
	# print(evalResults)
	lastKnee = currentKnee + 1

	while currentKnee < lastKnee:
		lastKnee = currentKnee
		currentKnee = LMethod([item for item in evalResults if item[0] <= cutoff])
		# print(currentKnee)
		cutoff = currentKnee * 2

	return currentKnee

# tool used for finding out the defining feature threshold
# performs linear regression
def linearReg(evalResults):
	x = np.array([xv for (xv,yv) in evalResults])
	y = np.array([yv for (xv,yv) in evalResults])
	# print(x, y)
	A = np.vstack([x, np.ones(len(x))]).T
	result = np.linalg.lstsq(A,y)
	m, c = result[0]
	residual = result[1]
	# print(result)
	return ((m, c), residual if len(evalResults) > 2 else 0)

# using the L-Method, find of the sweetspot for getting defining features
def LMethod(evalResults):
	# print(len(evalResults))
	if len(evalResults) < 4:
		return len(evalResults)
	# the partition point c goes from the second point to the -3'th point
	minResidual = np.inf
	minCons = None
	minCutoff = None
	for cidx in range(1, len(evalResults) - 2):
		(con1, residual1) = linearReg(evalResults[:cidx + 1])
		(con2, residual2) = linearReg(evalResults[cidx + 1:])
		if (residual1 + residual2) < minResidual:
			minCons = (con1, con2)
			minCutoff = cidx
			minResidual = residual1 + residual2
	if minCutoff == None:
		print('minCutoff is None', evalResults)
	return evalResults[minCutoff][0]

# assuming than the score distribution is linear initially
# then 3/4 of the scores should take up at least 1/2 of the L-method sweetspot
def getHalfPoint(scores):
	values = [x[1] for x in scores]
	total = sum(values) * 3 / 4
	cur = 0
	idx = 0
	for (idx, score) in scores:
		cur += score
		if cur > total:
			break
	# print('half point stats', idx, min(values), max(values))
	return idx

# used to calculate mutual information score for each feature
# cid_pattern_cnt records for each cluster the sum of each features
def getMutualInfoScore(clusters, sid_seq, idfMap, idxToSid, interestedCids, currentExclusions):
	cid_pattern_cnt = {}
	cid_user_cnt = {}
	for cluster in clusters:
		cid = cluster[3]
		sids = sorted([idxToSid[nidx] for nidx in cluster[0]]) # get the sorted list of all sids in the cluster
		# sids = [x + 1 for x in sids]	# add one to sid to get the actual sids
		feqMap = {}
		for sid in sids:
			for word in sid_seq[sid]:
				freq = sid_seq[sid][word]
				try:
					feqMap[word] += float(freq)
					# feqMap[word] += 1
				except:
					feqMap[word] = float(freq)
					# feqMap[word] = 1
		for word in currentExclusions:
			if word in feqMap:
				del feqMap[word]
		for word in feqMap:
			feqMap[word] *= idfMap[word]
		cid_pattern_cnt[cid] = feqMap
		cid_user_cnt[cid] = len(sids)
	return mutual_info.mutual_info_feature(cid_pattern_cnt, interested_cids = interestedCids)

# used to calculate chi square statistics for each feature
# chi_pattern_list records a list of feature weight for all instances in the cluster
# chi_user_cnt records for each cluster the number of instances
def getChiSquareScore(clusters, sid_seq, idfMap, idxToSid, interestedCids, currentExclusions):
	cid_pattern_list = {}
	cid_user_cnt = {}
	for cluster in clusters:
		cid = cluster[3]
		sids = sorted([idxToSid[nidx] for nidx in cluster[0]]) # get the sorted list of all sids in the cluster
		# sids = [x + 1 for x in sids]	# add one to sid to get the actual sids
		# print('in get chisquare')
		# print(sids)
		feqMap = {}
		for sid in sids:
			for word in sid_seq[sid]:
				if word in currentExclusions:
					continue
				idf = idfMap[word]
				freq = sid_seq[sid][word]
				try:
					feqMap[word].append(float(freq) * idf)
				except:
					feqMap[word] = [float(freq) * idf]
		for word in currentExclusions:
			if word in feqMap:
				del feqMap[word]
		cid_user_cnt[cid] = len(sids)
		cid_pattern_list[cid] = feqMap
	return mutual_info.chi_square_feature(cid_pattern_list, cid_user_cnt, interested_cids = interestedCids)

# determine which features to exclude
# clusterId => list of features to exclude
# clusterId => score of the excluded feature
# clusterId => all the features and their scores
def getExclusionMap(clusters, sid_seq, idfMap, idxToSid, interestedCids, currentExclusions = []):
	global excluTotal, excluLTotal
	# return dict([(row[3], []) for row in clusters]), {}
	excluStart = time.time()
	try:
		excluMethod = exclusionMethod
	except:
		excluMethod = 'chi'
	if excluMethod == 'chi':
		scoreMap = getChiSquareScore(clusters, sid_seq, idfMap, idxToSid, interestedCids, currentExclusions)
	elif excluMethod == 'mutual':
		scoreMap = getMutualInfoScore(clusters, sid_seq, idfMap, idxToSid, interestedCids, currentExclusions)
	else:
		return dict([(cid, []) for cid in interestedCids]), None
	# print('get exclusion taking %.4f' % (time.time() - excluStart))
	excluTotal += time.time() - excluStart

	excluLStart = time.time()

	exclusionMap = {}
	exclusionScoreMap = {}
	# print('dumping score map')
	# json.dump(scoreMap, open('scoreMap.json', 'w'))
	for cid in scoreMap:
		# using the L method to determine sweetspot
		scores = [(idx, scoreMap[cid][idx][1]) for idx in range(len(scoreMap[cid]))]
		# get the point where 75% of all scores are covered
		halfpoint = getHalfPoint(scores)
		# if the estimated maximum cut point is higher, use it, otherwise use 200
		# this is because we don't expect the number of features to be more than 200
		# so the halfpoint is just a precausion in case extreme cases happens
		breakPoint = getSweetSpotL(scores[:max(200, 2 * halfpoint)])
		# print('[LOG]: scores length %d, breakPoint %d' % (len(scores), breakPoint))

		# print(breakPoint)
		exclusionMap[cid] = [scoreMap[cid][idx] for idx in range(int(breakPoint + 1)) if idx <= breakPoint \
			and idx < len(scoreMap[cid])]
		# print(exclusionMap[cid])
		exclusionScoreMap[cid] = [x[1] for x in exclusionMap[cid]]
		exclusionMap[cid] = [x[0] for x in exclusionMap[cid]]
	# print(exclusionMap)
	# exit(0)
	# print('get exclusion cut point taking %.4f' % (time.time() - excluLStart))
	excluLTotal += time.time() - excluLStart

	return exclusionMap, exclusionScoreMap, scoreMap

# compute the basic values needed during modularity calculate
# so that we don't need to calculate it again and again
def modularityBasics(matrix):
	sumAll = 0
	sumEntries = array('L')
	totalItems = len(matrix)
	rowIdx = 0
	for row in matrix:
		sumEntry = sum([100 - row[idx] for idx in range(totalItems) if not idx == rowIdx])
		sumEntries.append(sumEntry)
		sumAll += sumEntry
		rowIdx += 1
	return (sumAll, sumEntries)

# calculate the modularity given cluster division
def evaluateModularity(clusters, outPath, matrix, basicInfo):
	# startTime = time.time()
	sumAll, sumEntries = basicInfo
	m = float(sumAll)
	firstEntry = 0
	secondEntry = 0
	for cluster in clusters:
		nodes = cluster[0]
		for nodeA in nodes:
			row = matrix[nodeA]
			for nodeB in nodes:
				firstEntry += 100 - row[nodeB]
				secondEntry += sumEntries[nodeA] * sumEntries[nodeB]
		for nodeA in nodes:
			firstEntry -= 100 - matrix[nodeA][nodeA]

	print(firstEntry)
	print(secondEntry)
	print(m)
	return (firstEntry - secondEntry / m ) / m

# the given cluster pair is the only change from the original clusterings
# which means this pair is splitted
# so the firstEntry should shift by
# -2 * sum of (100-node) for each node pair in clusterA * clusterB space
# the secondEntry should shift by
# -2 * sum of sumEntries[A] * sumEntries[B] for each node pair in the clusterA * clusterB space
def evaluateModularityShift(clusterPair, matrix, basicInfo):
	sumAll, sumEntries = basicInfo
	m = float(sumAll)
	firstEntry = 0
	secondEntry = 0
	for nodeA in clusterPair[0]:
		row = matrix[nodeA]
		for nodeB in clusterPair[1]:
			firstEntry += 100 - row[nodeB]
			secondEntry += sumEntries[nodeA] * sumEntries[nodeB]
	return -2 * (firstEntry - secondEntry / m) / m

def slidingMaxSweetSpot(evalResults, windowSize = 5):
	step = 1
	keys = [x[0] for x in evalResults.items()]
	start = min(keys) #+ 0.5 * windowSize
	end = max(keys) #- 0.5 * windowSize
	if (start >= end):
		print('[WARNING]: window too large when trying to determine sweetspot')
		return (start + end) / 2
	maxEval = min([x[1] for x in evalResults.items()])
	maxIdx = None
	while(start <= end):
		evals = [evalResults[idx] for idx in keys if start - 0.5 * windowSize <= idx <= start + 0.5 * windowSize]
		if len(evals) == 0:
			start += step
			continue
		avgEval = float(sum(evals)) / len(evals)
		# print start, avgEval
		if maxEval < avgEval:
			maxEval = avgEval
			maxIdx = start
		start += step
	return int(maxIdx + 0.5)

# given window size, calculate the sweetpot with mean on
# sliding window
def getSweetSpot(evalResults, windowSize = 5):
	result = slidingMaxSweetSpot(evalResults, windowSize)
	return result

def excludeFeatures(idfMap, exclusions):
	for feature in exclusions:
		idfMap[feature] = 0
	return idfMap

# get Gini coefficient of the chi-square / mutual info scores
# deterine whether it is skewed
# we assume that the input list is sorted reversely
def getGini(scoreList):
	totalScore = sum(scoreList)
	if totalScore == 0: return -1
	totalItem = len(scoreList)
	B = sum([scoreList[idx] * (0.5 + idx) for idx in range(totalItem)])
	B = float(B) / totalScore / totalItem
	A = 0.5 - B
	return A / (A + B)

# starting from full set
def runDiana(outPath, sid_seq, matrix = None, matrixPath = 'tmpMatrix.dat', idxToSid = None, totalItems = None, exclusions = [], clusterNum = -1):
	global splitTotal
	global modularityTotal
	global matrixCompTotal

	startTime = time.time()

	# fEval = open('%seval.txt' % (outPath), 'a+')

	idfMap = None

	isRoot = False

	# if the distance matrix is not provided, we will assume that it is the full matrix
	# and try to generat it
	if not matrix:
		matrix = []
		count = 1
		# isRoot = True
		rootResultPath = '%sroot_cluster.json' % outPath[:outPath.rindex('/') + 1]
		if not idxToSid:
			idxToSid = [x+1 for x in range(len(sid_seq))]

		if (matrixPath == None or not os.path.isfile(matrixPath)):
		# we don't need the full matrix
		# 	# write the full distance matrix
		# 	calculateDistance.fullMatrix(ngramPath, matrixPath)

			matrixStart = time.time()

			# generate the partical matrix and dump it into the file
			idfMap = excludeFeatures(getIdf(sid_seq, idxToSid), exclusions)
			matrix = calculateDistance.partialMatrix(idxToSid, \
				idfMap, ngramPath, 'tmp_%s_root' % int(time.time()), outPath, True)
			if matrixPath:
				fout = open(matrixPath, 'w')
				for row in matrix:
					fout.write('%s\n' % ('\t'.join(map(str, row))))
				fout.close()
			print('computing matrix taking %fs' % (time.time() - matrixStart))
			print('matrix size %s' % len(matrix))
			matrixCompTotal += time.time() - matrixStart
		else:
			# read the full distance matrix (after tf-idf)
			for line in open(matrixPath):
				matrix.append(array('B', map(int, line.split('\t'))))
				count += 1
				if (count % 500 == 0):
					print('%d lines read' % count)

	# idf map will later be used in feature selection
	if not idfMap:
		idfMap = excludeFeatures(getIdf(sid_seq, idxToSid), exclusions)

	matrixBasics = modularityBasics(matrix)
	print("--->")
	print(matrixBasics)
	print("--->")

	# for the full matrix, the sid is a faithful mapping
	if not idxToSid:
		idxToSid = [x+1 for x in range(len(matrix))]

	baseMatrix = matrix

	if not totalItems:
		totalItems = len(matrix)

	cid = 1
	clusters = [(range(len(matrix)), 100, None, cid)]

	# child Cid => parent Cid
	clusterHi = []

	# record the evaluation metrics
	evalResults = {}

	# if the root cluster is already stored and calculated, just read it
	if isRoot and os.path.isfile(rootResultPath):
		rootResult = json.load(open(rootResultPath))
		clusters = rootResult[0]
		sweetSpot = 0
		evalResults[0] = rootResult[1]
	else:
		# import cProfile
		# run until the biggest cluster is too small
		# TODO: add other stopping conditions, e.g. sweet spot
		while clusters[-1][1] and len(clusters[-1][0]) > sizeThreshold * totalItems:
		# while clusters[-1][1]:
		# run untill there is a small cluster, then stop
		# while clusters[-1][1]:
			# record the splitting tree
			parentCid = clusters[-1][3]
			clusterHi.append((parentCid, cid + 1, cid + 2))

			splitStartTime = time.time()
			# cProfile.runctx('splitCluster(clusters.pop(), baseMatrix)', globals(), locals())
			# exit(0)
			(clusterA, clusterB, baseSum) = (splitCluster(clusters.pop(), baseMatrix))
			# print('splitting cluster %f, %d' % (time.time() - splitStartTime, len(clusters)))
			splitTotal += time.time() - splitStartTime

			cid += 1
			clusters.append((clusterA, getDia(clusterA, baseMatrix), baseSum, cid))
			cid += 1
			clusters.append((clusterB, getDia(clusterB, baseMatrix), None, cid))

			# order first by diameter, then by size
			clusters = sorted(clusters, key = lambda x: (x[1], len(x[0])) if len(x[0]) > sizeThreshold * totalItems else (0,0))

			moduStartTime = time.time()
			if len(clusters) == 2:
				# if it is the first time to compute modularity
				evalResult = evaluateModularity(clusters, outPath, baseMatrix, matrixBasics)
			else:
				# if it is based on the previous scores
				evalResult = evalResults[len(clusters) - 1] + evaluateModularityShift((clusterA, clusterB), baseMatrix, matrixBasics)
			# print('evaluateClustersModularity %f' % (time.time() - moduStartTime))
			modularityTotal += time.time() - moduStartTime
			# print(evalResult, moduResult, orgResult)

			evalResults[len(clusters)] = evalResult

		# if the clusterNum is specified, do a simple clustering without hierarchy
		if clusterNum > 0:
			sweetSpot = clusterNum
		else:
			sweetSpot = getSweetSpot(evalResults)
			print('[LOG]: split %s into %d clusters (modularity %f)' % \
				(outPath, sweetSpot, evalResults[sweetSpot]))

		# merge the clusters to the point of sweet spot
		clusterMap = dict([(row[3],row) for row in clusters])
		while(len(clusters) > sweetSpot):
			(parentCid, childACid, childBCid) = clusterHi.pop()
			# dismeter doesn't matter, so put zero here
			clusterMap[parentCid] = (clusterMap[childACid][0] + clusterMap[childBCid][0], 0, None, parentCid)
			clusters.append(clusterMap[parentCid])
			clusters.remove(clusterMap[childACid])
			clusters.remove(clusterMap[childBCid])

	if isRoot and not os.path.isfile(rootResultPath):
		json.dump([clusters, evalResults[sweetSpot]], open(rootResultPath, 'w'))

	# get the exclusion map according to the current clustering
	# excludeMap, scoreMap = getExclusionMap(clusters, sid_seq, idfMap, idxToSid, \
	# 	[row[3] for row in clusters if len(row[0]) > sizeThreshold * totalItems], exclusions)
	excludeMap, exclusionScoreMap, scoreMap = getExclusionMap(clusters, sid_seq, idfMap, idxToSid, \
		[row[3] for row in clusters], exclusions)
	# fEval.write(json.dumps(scoreMap))

	# for each cluter, we start a new clustering
	results = []
	# print([len(cluster[0]) for cluster in clusters])
	for cidx in range(len(clusters)):
		row = clusters[cidx]
		idxs = row[0]	# get the list of all node in the cluster
		sids = sorted([idxToSid[nidx] for nidx in idxs]) # get the sorted list of all sids in the cluster
		excludedFeatures = excludeMap[row[3]]
		excludedScores = exclusionScoreMap[row[3]]
		# if we want to continute cluster this subcluster
		if len(sids) > sizeThreshold * totalItems:
			newExclusions = exclusions + excludedFeatures
			# remove sids where the vector have all zeros
			newExclusionSet = set(newExclusions)
			oldLen = len(sids)
			excludedSids = [sid for sid in sids if len(set(sid_seq[sid].keys()) - newExclusionSet) == 0]
			sids = [sid for sid in sids if len(set(sid_seq[sid].keys()) - newExclusionSet) > 0]
			# print(oldLen, len(sids), len(excludedFeatures), excludedFeatures)
			# if the cluster size is too small after feature selection, don't cluster it
			if not len(sids) > sizeThreshold * totalItems:
				result = ('l', sids + excludedSids, {'exclusions': excludedFeatures, \
					'exclusionsScore': excludedScores})
			else:
				matrixStart = time.time()
				matrix = calculateDistance.partialMatrix(sids, \
					excludeFeatures(getIdf(sid_seq, sids), newExclusions), ngramPath, \
					'tmp_%d' % row[3], '%st%d_' % (outPath, row[-1]), True)
				# print('computing matrix taking %fs' % (time.time() - matrixStart))
				matrixCompTotal += time.time() - matrixStart
				# now that we have a new distance matrix, go and do another round of clustering
				result = runDiana('%sp%d_' % (outPath, row[-1]), sid_seq, matrix, idxToSid = sids, totalItems = totalItems,\
					exclusions = newExclusions)
				if len(result) > 2:
					info = result[2]
				else:
					info = {}
				# put the excluded sids back as a cluster
				if (len(excludedSids) > 0):
					result[1].append(('l', excludedSids, {'isExclude':True}))

				info['exclusions'] = excludedFeatures
				info['exclusionsScore'] = excludedScores
				# base on the score map, calculate the gini coefficient
				# score map format {cid:[(feature, score)]}
				info['gini'] = getGini([x[1] for x in scoreMap[row[3]]])
				# fEval.write('gini %s : %f\n' % ('%sp%d_' % (outPath, row[3]), info['gini']))
				result = (result[0], result[1], info)
		else:
			result = ('l', sids, {'exclusions': excludedFeatures, \
				'exclusionsScore': excludedScores})

		results.append(result)

	return ('t', results, {'sweetspot': evalResults[sweetSpot]})

# contructe a json representation of the final clustering result
# including uids, excluded features, gini coeff, etc.
def richerTree(tree, sidUidM, startCid = 1):
	if tree[0] == 'l':
		if len(tree[1]) == 0:
			return startCid, None
		uids = reduce(lambda x, y: x + y, [sidUidM[sid] for sid in tree[1]])
		if len(tree) == 3:
			result = tree[2]
		else:
			result = {}
		result['type'] = 'l'
		result['uids'] = uids
		# result['clusterId'] = startCid
		return startCid + 1, result
	subTrees = []
	for subTree in tree[1]:
		startCid, newSubTree = richerTree(subTree, sidUidM, startCid)
		if newSubTree:
			subTrees.append(newSubTree)
	if len(tree) == 3:
		result = tree[2]
	else:
		result = {}
	result['type'] = 't'
	result['children'] = subTrees
	return startCid, result


# this function is a simpler version of the main function
# it is a wrapper around runDiana
# intended to be used for rhcBootstrap
# sid_seq is the complete mapping between sid and seqCnt
# matrixPath is the path to store the root matrix for this clustering
# outPath is the path to store the result / temp files
# sids is the subset chosen to perform clustering on
def run(sid_seq, outPath, matrixPath, sids, nPath):
	global sizeThreshold
	global ngramPath
	ngramPath = nPath
	sizeThreshold = 0.05
	return runDiana(outPath, sid_seq, None, matrixPath, sorted(sids))


matrixCompTotal = 0
modularityTotal = 0
splitTotal = 0
excluTotal = 0
excluLTotal = 0

if __name__ == '__main__':

	ngramPath = sys.argv[1]
	outPath = sys.argv[2]
	sizeThreshold = float(sys.argv[3]) if len(sys.argv) > 3 else 0.05

	matrixPath = '%smatrix.dat' % outPath
	resultPath = '%sresult.json' % outPath

	startTime = time.time()
	sid_seq = getSidNgramMap(ngramPath)
	print('[LOG]: total users %d' % len(sid_seq))
	treeData = runDiana(outPath, sid_seq, matrixPath = matrixPath)
	json.dump(treeData, open(resultPath, 'w'))
	print('[STAT]: total time %f' % (time.time() - startTime))
	print('[STAT]: maxtrix com: %f, modularity: %f, split: %f, exclusion score: %f, exclusion cut: %f' % \
	 	(matrixCompTotal, modularityTotal, splitTotal, excluTotal, excluLTotal))
