package org.terasology.module.resources;

import org.terasology.util.Varargs;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * ModuleFileSource provides an interface for all providers of files (resources) that are part
 * of the content of a Module. This includes file discovery and reading.
 *
 * As a number of different mechanisms can be used to provide files, ModuleFileSource provides a
 * simplified view, where:
 * <ul>
 *     <li>A file path is a list of strings, each representing a folder or step in the path</li>
 *     <li>It is possible to discover what paths are within a path</li>
 *     <li>It is possible to discover what files are within a path, optionally recursively</li>
 *     <li>A file can be streamed</li>
 * </ul>
 */
public interface ModuleFileSource extends Iterable<ModuleFile> {

    /**
     * Obtain the handle to a specific file
     * @param path The path to the file
     * @param morePath More path to the file
     * @return The requested file, or {@link Optional#empty()} if it doesn't exist
     */
    default Optional<ModuleFile> getFile(String path, String ... morePath) {
        return getFile(Varargs.combineToList(path, morePath));
    }

    /**
     * Obtain the handle to a specific file
     * @param filepath The path to the file. Should not be empty
     * @return The requested file, or {@link Optional#empty()} if it doesn't exist
     */
    Optional<ModuleFile> getFile(List<String> filepath);

    /**
     * @return A collection of all files provided by this ModuleFileSource
     */
    default Collection<ModuleFile> getFiles() {
        return getFilesInPath(true);
    }

    /**
     * Finds all files within a path
     * @param recursive Whether to recurse through subpaths
     * @param path The path to search
     * @return A collection of handles to all files in the give path
     */
    default Collection<ModuleFile> getFilesInPath(boolean recursive, String ... path) {
        return getFilesInPath(recursive, Arrays.asList(path));
    }

    /**
     * Finds all files within a path
     * @param recursive Whether to recurse through subpaths
     * @param path The path to search
     * @return A collection of handles to all files in the give path
     */
    Collection<ModuleFile> getFilesInPath(boolean recursive, List<String> path);

    /**
     * Finds all subpaths in the given path
     * @param fromPath The path to search
     * @return A list of the immediate subpaths in the given path
     */
    default Set<String> getSubpaths(String ... fromPath) {
        return getSubpaths(Arrays.asList(fromPath));
    }

    /**
     * Finds all subpaths in the given path
     * @param fromPath The path to search
     * @return A list of the immediate subpaths in the given path
     */
    Set<String> getSubpaths(List<String> fromPath);

}