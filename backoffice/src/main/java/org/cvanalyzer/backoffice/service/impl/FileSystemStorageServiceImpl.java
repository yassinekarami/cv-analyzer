package org.cvanalyzer.backoffice.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Stream;

import org.cvanalyzer.backoffice.exception.StorageException;
import org.cvanalyzer.backoffice.exception.StorageFileNotFoundException;
import org.cvanalyzer.backoffice.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Implementation of {@link StorageService} that stores files
 * on the local file system.
 *
 * <p>
 * This service provides methods for storing, loading,
 * retrieving, and deleting files from a configured root directory.
 * </p>
 */
@Service
public class FileSystemStorageServiceImpl implements StorageService {

    /**
     * Default folder name where uploaded files are stored.
     */
    private String path = "upload-dir";

    /**
     * Root location used for file storage operations.
     */
    private final Path rootLocation;

    /**
     * Creates a new file system storage service instance.
     *
     * <p>
     * Initializes the root storage location using the configured path.
     * </p>
     */
    @Autowired
    public FileSystemStorageServiceImpl() {
        this.rootLocation = Paths.get(path);
    }

    /**
     * Stores the given multipart file in the configured storage directory.
     *
     * <p>
     * The file is copied to the target location and replaces any
     * existing file with the same name.
     * </p>
     *
     * @param file the multipart file to store
     * @throws StorageException if the file is empty, invalid,
     *                          or cannot be stored
     */
    @Override
    public void store(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file.");
            }

            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize()
                    .toAbsolutePath();

            // Security check to prevent path traversal attacks
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(
                        inputStream,
                        destinationFile,
                        StandardCopyOption.REPLACE_EXISTING
                );
            }

        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    /**
     * Loads all stored files from the root storage directory.
     *
     * @return a stream containing relative paths of all stored files
     * @throws StorageException if files cannot be read
     */
    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);

        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    /**
     * Resolves the given filename against the root storage location.
     *
     * @param filename the name of the file to load
     * @return the resolved file path
     */
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    /**
     * Loads a file as a Spring {@link Resource}.
     *
     * <p>
     * The resource can then be used for file download or streaming.
     * </p>
     *
     * @param filename the name of the file to load
     * @return the file resource
     * @throws StorageFileNotFoundException if the file does not exist
     *                                      or cannot be accessed
     */
    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);
            }

        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException(
                    "Could not read file: " + filename,
                    e
            );
        }
    }

    /**
     * Loads multiple files as Spring {@link Resource} objects.
     *
     * @param filesName list of filenames to load
     * @return list of file resources
     */
    @Override
    public List<Resource> loadAsResources(List<String> filesName) {
        return filesName.stream()
                .map(this::loadAsResource)
                .toList();
    }

    /**
     * Deletes all files and directories inside the root storage location.
     */
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /**
     * Initializes the storage directory.
     *
     * <p>
     * Creates the root directory if it does not already exist.
     * </p>
     *
     * @throws StorageException if the storage directory
     *                          cannot be initialized
     */
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);

        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}