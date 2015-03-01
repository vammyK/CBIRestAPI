package org.cbir.retrieval.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.cbir.retrieval.domain.Storach;
import org.cbir.retrieval.security.AuthoritiesConstants;
import org.cbir.retrieval.service.RetrievalService;
import org.cbir.retrieval.web.rest.dto.RetrievalServerJSON;
import org.cbir.retrieval.web.rest.dto.StorageJSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import retrieval.server.RetrievalServer;
import retrieval.storage.Storage;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing storage.
 */
@RestController
@RequestMapping("/api")
public class StorageResource {

    private final Logger log = LoggerFactory.getLogger(StorageResource.class);

    @Inject
    private RetrievalService retrievalService;

    /**
     * POST -> Create a new storage.
     */
    @RequestMapping(value = "/storages",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void create(@RequestBody StorageJSON storageJSON) {
        log.debug("REST request to save storage : {}", storageJSON.getId());
        RetrievalServer retrievalServer = retrievalService.getRetrievalServer();

        if(idStorage==null)
            throw new IllegalArgumentException("idSotrage is null");

        if(retrievalServer.getStorage(idStorage)!=null) {
            throw new IllegalArgumentException("Storage "+ idStorage +" already exist!");
        }
        try {
            retrievalServer.createStorage(storageJSON.getId());
        } catch(Exception e) {
              log.error(e.getMessage());
        }

    }

    @RequestMapping(value="/storages",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Timed
    @RolesAllowed(AuthoritiesConstants.USER)
    List<StorageJSON> getAll() {
        log.debug("REST request to list storages : {}");

        RetrievalServer retrievalServer = retrievalService.getRetrievalServer();

        List<StorageJSON> storagesJSON =
            retrievalServer.getStorageList()
                .stream()
                .map(StorageJSON::new)
                .collect(Collectors.toList());

        return storagesJSON;
    }


    @RequestMapping(value = "/storages/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @RolesAllowed(AuthoritiesConstants.USER)
    ResponseEntity<StorageJSON> get(@PathVariable String id) {
        log.debug("REST request to get storage : {}");

        RetrievalServer retrievalServer = retrievalService.getRetrievalServer();
        Storage storage = retrievalServer.getStorage(id);

        return Optional.ofNullable(storage)
            .map(itStorage -> new ResponseEntity<>(new StorageJSON(itStorage), HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rest/storachs/:id -> delete the "id" storach.
     */
    @RequestMapping(value = "/storages/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable String id) {
        log.debug("REST request to delete storage : {}", id);
        RetrievalServer retrievalServer = retrievalService.getRetrievalServer();

//        if(retrievalServer.getStorage(id)==null) {
//            throw new IllegalArgumentException("Storage "+ idStorage +" not exist!");
//
        try {
            retrievalServer.deleteStorage(id);
        } catch(Exception e) {
            log.error(e.getMessage());
        }

    }

}
