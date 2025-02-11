package com.projects.UrlShortner.Contoller;


import com.projects.UrlShortner.Model.UrlEntity;
import com.projects.UrlShortner.Service.UrlShorteningService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/shortener")
public class UrlShorteningController {

    @Autowired
    UrlShorteningService urlShorteningService;

    @PostMapping("/createShortUrl")
    public ResponseEntity<?> createShortUrl(@Valid @RequestBody UrlEntity urlEntity)
    {
       if(urlEntity.getUrl() != null)
       {
           UrlEntity createdUrl = urlShorteningService.generateShortUrl(urlEntity.getUrl());
           Map<String, Object> serializableObj = new  HashMap<>();
           serializableObj.put("id", createdUrl.getShortCode());
           serializableObj.put("url", createdUrl.getUrl());
           serializableObj.put("shortCode", createdUrl.getShortCode());
           serializableObj.put("updatedOn", createdUrl.getUpdatedOn());
           serializableObj.put("createdOn",createdUrl.getCreatedOn());

           return new ResponseEntity<>(serializableObj, HttpStatus.CREATED);
       }
       else
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);


    }

    @GetMapping("/getAllUrls")
    public ResponseEntity<?> getAllUrls()
    {
        List<UrlEntity> urls = urlShorteningService.getAllUrls();
        if(!urls.isEmpty())
        {
            return new ResponseEntity<>(urls,HttpStatus.OK);
        }
        else
            return new ResponseEntity<>("No data present",HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/deleteUrl/{code}")
    public ResponseEntity<?> deleteUrl(@PathVariable String code)
    {
         boolean deleted = urlShorteningService.deleteUrl(code);
         if(deleted)
         {
             return new ResponseEntity<>("Deleted",HttpStatus.OK);
         }
         else
             return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

    @PutMapping("/updateUrl/{code}")
    public ResponseEntity<?> updateOriginalUrl(@PathVariable String code, @Valid @RequestBody UrlEntity urlEntity)
    {
        Optional<UrlEntity> url = urlShorteningService.updateShortCode(code, urlEntity.getUrl());
        if(url.isPresent())
        {
            UrlEntity updatedUrl = url.get();
            Map<String,Object> serializableObj = new HashMap<>();
            serializableObj.put("id", updatedUrl.getShortCode());
            serializableObj.put("url", updatedUrl.getUrl());
            serializableObj.put("shortCode", updatedUrl.getShortCode());
            serializableObj.put("updatedOn", updatedUrl.getUpdatedOn());
            serializableObj.put("createdOn",updatedUrl.getCreatedOn());

            return new ResponseEntity<>(serializableObj,HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getStats/{code}")
    public ResponseEntity<?> getStats(@PathVariable String code)
    {
        Optional<UrlEntity> url = urlShorteningService.getStatistics(code);
        if(url.isPresent())
        {
            UrlEntity existingUrl = url.get();
            Map<String,Object> serializableObj = new HashMap<>();
            serializableObj.put("id", existingUrl.getShortCode());
            serializableObj.put("url", existingUrl.getUrl());
            serializableObj.put("shortCode", existingUrl.getShortCode());
            serializableObj.put("updatedOn", existingUrl.getUpdatedOn());
            serializableObj.put("createdOn",existingUrl.getCreatedOn());
            serializableObj.put("clickedOn", existingUrl.getClickedTimes());

            return new ResponseEntity<>(serializableObj,HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortCode)
    {
        Optional<UrlEntity> urlMapping = urlShorteningService.getOriginalUrl(shortCode);
        if (urlMapping.isPresent())
        {
            UrlEntity redirectTo = urlMapping.get();//I am getting the value from the optional.
            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", redirectTo.getUrl())
                    .build();
        }
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }




}
