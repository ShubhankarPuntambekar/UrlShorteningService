package com.projects.UrlShortner.Service;

import com.projects.UrlShortner.Model.UrlEntity;
import com.projects.UrlShortner.Repository.UrlEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UrlShorteningService {

    @Autowired
    private UrlEntityRepository urlEntityRepository;

    private static final String baseChangeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int shortCodeLength = 7;

    @Transactional
    public UrlEntity generateShortUrl(String originalUrl)
    {
        String code = generateHashCode();
        UrlEntity url = new UrlEntity();
        url.setUrl(originalUrl);
        url.setShortCode(code);
        return urlEntityRepository.save(url);

    }

    private String generateHashCode()
    {
        StringBuilder sb = new StringBuilder(shortCodeLength);
        String hashCode;
        for(int i=0;i<shortCodeLength;i++)
        {
            sb.append(baseChangeChars.charAt((int)Math.floor(Math.random()*10)));
        }
        hashCode = sb.toString();
        return hashCode;
    }

    @Transactional
    public Optional<UrlEntity> getOriginalUrl(String code)
    {
        Optional<UrlEntity> urlEntity = urlEntityRepository.findById(code);
        if(urlEntity.isPresent())
        {
            UrlEntity url = urlEntity.get();
            url.setClickedTimes(url.getClickedTimes()+1);
            return Optional.of(urlEntityRepository.save(url));
        }
        return Optional.empty();

    }

    @Transactional(readOnly = true)
    public Optional<UrlEntity> getStatistics(String code)
    {
        return urlEntityRepository.findById(code);
    }

    @Transactional
    public Optional<UrlEntity> updateShortCode(String code, String newUrl)
    {
        Optional<UrlEntity> urlEntity = urlEntityRepository.findById(code);
        if(urlEntity.isPresent())
        {
            UrlEntity url = urlEntity.get();
            url.setUrl(newUrl);
            url.setUpdatedOn(LocalDateTime.now());
            return Optional.of(urlEntityRepository.save(url));//Creates an optional of url.
        }
        return Optional.empty();
    }

    @Transactional
    public List<UrlEntity> getAllUrls()
    {
        return urlEntityRepository.findAll();
    }

    @Transactional
    public boolean deleteUrl(String code)
    {
        boolean exists = urlEntityRepository.existsById(code);
        if(exists)
        {
            urlEntityRepository.deleteById(code);
            return true;
        }
        else
            return false;
    }



}
