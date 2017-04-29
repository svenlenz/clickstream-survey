import multiprocessing, json, sys, math, subprocess, os, glob, cPickle, time
from array import *
import datetime

config = json.load(open('servers.json'))
# thread number
THREAD_NUM = config['threadNum']

# the minimum slice size
MIN_SLICE = config['minPerSlice']

# the minimum size per server
MIN_SERVER = THREAD_NUM * MIN_SLICE

PI_VALUE = 3.141592653589793


class myThread (multiprocessing.Process):

	def getSeqDist(self, dic1, dic2):
		dist = 0
		# vlist1 = []
		# vlist2 = []

		gramset = list(set(dic1.keys()) | set(dic2.keys()))
		vlist1 = [dic1[g] if g in dic1 else 0 for g in gramset]
		vlist2 = [dic2[g] if g in dic2 else 0 for g in gramset]
		# for g in gramset:
		# 	v1 = 0
		# 	v2 = 0
		# 	# try:
		# 	# 	idfG = self.idfMap[g]
		# 	# except:
		# 	# 	idfG = 1
		# 	# 	# print('no idfG')
		# 	# if g in dic1: v1 = int(dic1[g] * idfG)
		# 	# if g in dic2: v2 = int(dic2[g] * idfG)

		# 	if g in dic1: v1 = int(dic1[g])
		# 	if g in dic2: v2 = int(dic2[g])
		# 	vlist1.append(v1)
		# 	vlist2.append(v2)

		dist = 1.0001 - angle(vlist1, vlist2)/(PI_VALUE/2)

		if dist<=0:
			#continue 
			dist= 0.00000001
		dist = float(dist)*100
		weight = int(dist)+1
		if weight > 100:
			weight = 100
		return 100 - weight

	def __init__(self, threadID, prefix, sid_seq, sfrom, sto, idfMap):
		self.threadID = threadID
		self.sid_seq = sid_seq
		self.sfrom = sfrom
		self.sto = sto
		self.idfMap = idfMap
		#self.fo = gzip.open(prefix+'_'+str(srange[0])+'-'+str(srange[1])+'.gz', 'w')
		self.fo = open(prefix+'_'+str(sfrom[0]), 'w')
		# print(prefix+'_'+str(sfrom[0]))
		multiprocessing.Process.__init__(self)

	def run(self):
		# pr = cProfile.Profile()
		# pr.enable()

		print '[LOG]: start new thread '+str(self.threadID)
		for sidFrom in self.sfrom:
			dic1 = self.sid_seq[sidFrom]
			dists = []
			for sidTo in self.sto:
				if sidFrom == sidTo:
					dists.append(0)
					continue
				dic2 = self.sid_seq[sidTo]

				dists.append(self.getSeqDist(dic1, dic2))
			# print(min(dists))
			self.fo.write('\t'.join(map(str, dists)) + '\n')
		self.fo.close() 

def angle(v1, v2):
	norm1 = 0
	norm2 = 0
	dotprod = 0
	for i in range(0, len(v1)):
		# norm1 += v1[i]*v1[i]
		# norm2 += v2[i]*v2[i]
		dotprod += v1[i]*v2[i]
	# a = 0
	a = dotprod
	# if norm1>0 and norm2>0: 
	# 	a = 1.0*dotprod/(math.sqrt(norm1) * math.sqrt(norm2))
	if a > 1:
		a = 1
	return math.acos(a)
	
# @inputPath : sampleREAL/subsetall1k/all_setid_ngramcnt_time_sid_sample
# @sidsPath: the path to two lists of sids that we need to calculate, one as from and one as to
# @outputPath : where final path is placed
def calDist(inputPath, sidsPath, outputPath, tmpPrefix = '', idfMapPath = None):
	# print(sidsPath, os.getcwd(), tmpPrefix, idfMapPath)
	print('[LOG]: %s computing matrix for %s' % (datetime.datetime.now(), sidsPath.split('sid_')[0]))
	# read the ngram file, generate a node list
	lineNum = 1
	sids = cPickle.load(open(sidsPath))

	fromSids = set(sids[0])
	# in principle the toSids should be all sids
	toSids = set(sids[1])

	startTime = time.time()

	# get the idfMap, if provided
	if (idfMapPath):
		idfMap = cPickle.load(open(idfMapPath))
	else:
		idfMap = None

	sid_seq = {}
	for line in open(inputPath):
		# get the sid
		sid = int(line.split('\t')[0])
		if (not sid in toSids and not sid in fromSids):
			continue
		# get the ngram
		line = line.strip()
		line = line.split('\t')[1]
		# remove the trailing ) so that the spliting would not have an empty tail
		line = line[:-1].split(')')
		if idfMap:
			curSeq = [(item[0], int(idfMap[item[0]] * int(item[1]))) for item in map(lambda x: x.split('('), line)]
		else:
			curSeq = [(item[0], int(item[1])) for item in map(lambda x: x.split('('), line)]
		# normalized curSeq by its length
		lenSeq = math.sqrt(sum([x[1] ** 2 for x in curSeq]))
		sid_seq[sid] = dict([(x, y / lenSeq) for (x,y) in curSeq])
	
	# slice fromSids into THREAD_NUM pieces
	if (len(fromSids) < MIN_SLICE * THREAD_NUM):
		step = MIN_SLICE
	else:
		step = len(fromSids) / THREAD_NUM + 1

	# print('preprocessing takes %.4fs' % (time.time() - startTime))

	tid=0
	threads = []
	start = 0
	while start < len(fromSids):
		tid += 1
		thread = myThread(tid, '%s%sdist' % (outputPath, tmpPrefix), sid_seq, sids[0][start:start+step], sids[1], idfMap)
		thread.start()
		threads.append(thread)
		start += step
		
	# wait unitl thread ends
	for t in threads:
		t.join()

	# print('everything takes %.4fs' % (time.time() - startTime))

# at this point the outputPath should have been made
# instead of writing a file, we return the matrix
# realSid: indicates whether the sid index is actually offset by 1, so that the lowest sid is 0
def partialMatrix(sids, idfMap, ngramPath, tmpPrefix, outputPath, realSid = False):
	servers = json.load(open('servers.json'))['server']
	if not realSid:
		sids = [x + 1 for x in sids]
	total = len(sids)
	if (total < MIN_SERVER * len(servers)):
		step = MIN_SERVER
	else:
		step = total / len(servers) + 1
	processes = []
	start = 0
	cPickle.dump(idfMap, open('%s%sidf.pkl' % (outputPath, tmpPrefix), 'w'))
	for server in servers:
		if (start >= total):
			break
		cPickle.dump([sids[start:start+step], sids], open('%s%ssid_%s.pkl' % (outputPath, tmpPrefix, server), 'w'))
		print('[LOG]: starting in %s for %s' % (server, tmpPrefix))
		# print(['ssh', server, \
		# 	'cd %s\npython calculateDistance.py %s %s%ssid_%s.pkl %s %s %s%sidf.pkl' % (os.getcwd(), ngramPath, outputPath, \
		# 		tmpPrefix, server, outputPath, tmpPrefix, outputPath, tmpPrefix)])
		# return
		if server == 'localhost':
			calDist(ngramPath, '%s%ssid_%s.pkl' % (outputPath, tmpPrefix, server), \
				outputPath, tmpPrefix, '%s%sidf.pkl' % (outputPath, tmpPrefix))
		else:
			processes.append(subprocess.Popen(['ssh', server, \
				'cd %s\npython calculateDistance.py %s %s%ssid_%s.pkl %s %s %s%sidf.pkl' % (os.getcwd(), ngramPath, outputPath, \
					tmpPrefix, server, outputPath, tmpPrefix, outputPath, tmpPrefix)]))
		start += step

	for process in processes:
		process.wait()


	files = sorted(glob.glob('%s%sdist_*' % (outputPath, tmpPrefix)), key = lambda x: int(x.split('_')[-1]))
	# print(files)
	matrix = []
	for fname in files:
		with open(fname) as infile:
			for line in infile:
				matrix.append(array('B', map(int, line.split('\t'))))
	# print('[LOG]: merge finished for %s%s' % (outputPath, tmpPrefix))

	for fname in glob.glob('%s%s*' % (outputPath, tmpPrefix)):
		os.remove(fname)
	# print('[LOG]: all tmp files removed for %s%s' % (outputPath, tmpPrefix))
	print('[LOG]: %s matrix computation finished for %s%s' % (
		datetime.datetime.now(), outputPath, tmpPrefix))

	return matrix


def fullMatrix(inputPath, outputPath):
	outputFile = outputPath
	outputPathDir = outputPath[:outputPath.rfind('/')]
	try:
		print('makedir: %s' % outputPathDir)
		os.mkdir(outputPathDir)
	except:
		pass
	servers = json.load(open('servers.json'))
	# get all the sids
	sids = [int(line.split('\t')[0]) for line in open(inputPath).readlines()]
	total = len(sids)
	if (total < MIN_SERVER * len(servers)):
		step = MIN_SERVER
	else:
		step = total / len(servers) + 1

	processes = []
	start = 0
	for server in servers:
		if (start >= total):
			break
		cPickle.dump([sids[start:start+step], sids], open('%ssid_%s.pkl' % (outputPath, server), 'w'))
		print('starting in %s' % server)
		processes.append(subprocess.Popen(['ssh', server, \
			'cd %s\npython calculateDistance.py %s %ssid_%s.pkl %s' % (os.getcwd(), inputPath, outputPath, server, outputPath)]))
		start += step

	for process in processes:
		process.wait()

	print('all finished')

	# find subsetall1k -name "dist_*" | sort -t _ -k 2 -g | xargs cat > subsetall1k/all_dist.dat
	# subprocess.call(shlex.split('find %s -name "dist_*" | sort -t _ -k 2 -g | xargs cat > %s/all_dist.dat' % (outputPath, outputPath)))
	files = sorted(glob.glob('%sdist_*' % outputPath), key = lambda x: int(x.split('_')[-1]))
	print(files)
	with open(outputFile, 'w') as outfile:
		for fname in files:
			with open(fname) as infile:
				for line in infile:
					outfile.write(line)
	print('merge Finished')

	for fname in glob.glob('%sdist_*' % outputPath) + glob.glob('%ssid_*' % outputPath):
		os.remove(fname)

	print('all tmp files removed')


if __name__ == "__main__":
	if (sys.argv[1] == 'full'):
		fullMatrix(sys.argv[2], sys.argv[3])
	else:
		if (len(sys.argv) > 5):
			calDist(sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4], sys.argv[5])
		else:
			calDist(sys.argv[1], sys.argv[2], sys.argv[3])