package com.mns.mojoinvest.server.resource;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.sun.jersey.api.view.Viewable;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Path("/loader")
public class UploadResource {

    private static final Logger log = Logger.getLogger(UploadResource.class.getName());

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable getUploadForm() {
        BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
        String uploadUrl = blobstoreService.createUploadUrl("/upload");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("uploadUrl", uploadUrl);
        log.fine("Upload url: " + uploadUrl);
        return new Viewable("/upload.mustache", map);
    }


}
