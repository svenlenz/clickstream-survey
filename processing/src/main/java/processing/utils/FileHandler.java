package processing.utils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * file handlers using java 7 nio. 
 * 
 * @author sven.lenz@msc.htwchur.ch
 */
public class FileHandler {

	public static void removeRecursive(Path path) throws IOException
	{
	    Files.walkFileTree(path, new SimpleFileVisitor<Path>()
	    {
	        @Override
	        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
	                throws IOException
	        {
	            Files.deleteIfExists(file);
	            return FileVisitResult.CONTINUE;
	        }

	        @Override
	        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
	        {
	            // try to delete the file anyway, even if its attributes
	            // could not be read, since delete-only access is
	            // theoretically possible
	            Files.deleteIfExists(file);
	            return FileVisitResult.CONTINUE;
	        }

	        @Override
	        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
	        {
	            if (exc == null)
	            {
	                Files.delete(dir);
	                return FileVisitResult.CONTINUE;
	            }
	            else
	            {
	                // directory iteration failed; propagate exception
	                throw exc;
	            }
	        }
	    });
	}
	
	public static void copyFiles(Path sourcePath, Path targetPath) throws IOException {

//		InficonFileFilter iff = new InficonFileFilter();
		
	    	    Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
	    	        @Override
	    	        public FileVisitResult preVisitDirectory(final Path dir,
	    	                final BasicFileAttributes attrs) throws IOException {
	    	            Files.createDirectories(targetPath.resolve(sourcePath
	    	                    .relativize(dir)));
	    	            return FileVisitResult.CONTINUE;
	    	        }

	    	        @Override
	    	        public FileVisitResult visitFile(final Path file,
	    	                final BasicFileAttributes attrs) throws IOException {
	    	        	
//	    	        	if(iff.accept(file.toFile())) {	    	        	
		    	            Files.copy(file,
		    	                    targetPath.resolve(sourcePath.relativize(file)));
//	    	            }
	    	            return FileVisitResult.CONTINUE;
	    	        }
	    	    });
	}
}
