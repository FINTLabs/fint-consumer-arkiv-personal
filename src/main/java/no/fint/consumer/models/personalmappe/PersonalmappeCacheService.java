package no.fint.consumer.models.personalmappe;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

import no.fint.cache.CacheService;
import no.fint.cache.model.CacheObject;
import no.fint.consumer.config.Constants;
import no.fint.consumer.config.ConsumerProps;
import no.fint.consumer.event.ConsumerEventUtil;
import no.fint.event.model.Event;
import no.fint.event.model.ResponseStatus;
import no.fint.relations.FintResourceCompatibility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import no.fint.model.arkiv.personal.Personalmappe;
import no.fint.model.resource.arkiv.personal.PersonalmappeResource;
import no.fint.model.arkiv.personal.PersonalActions;
import no.fint.model.felles.kompleksedatatyper.Identifikator;

@Slf4j
@Service
@ConditionalOnProperty(name = "fint.consumer.cache.disabled.personalmappe", havingValue = "false", matchIfMissing = true)
public class PersonalmappeCacheService extends CacheService<PersonalmappeResource> {

    public static final String MODEL = Personalmappe.class.getSimpleName().toLowerCase();

    @Value("${fint.consumer.compatibility.fintresource:true}")
    private boolean checkFintResourceCompatibility;

    @Autowired
    private FintResourceCompatibility fintResourceCompatibility;

    @Autowired
    private ConsumerEventUtil consumerEventUtil;

    @Autowired
    private ConsumerProps props;

    @Autowired
    private PersonalmappeLinker linker;

    private JavaType javaType;

    private ObjectMapper objectMapper;

    public PersonalmappeCacheService() {
        super(MODEL, PersonalActions.GET_ALL_PERSONALMAPPE, PersonalActions.UPDATE_PERSONALMAPPE);
        objectMapper = new ObjectMapper();
        javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, PersonalmappeResource.class);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @PostConstruct
    public void init() {
        props.getAssets().forEach(this::createCache);
    }

    @Scheduled(initialDelayString = Constants.CACHE_INITIALDELAY_PERSONALMAPPE, fixedRateString = Constants.CACHE_FIXEDRATE_PERSONALMAPPE)
    public void populateCacheAll() {
        props.getAssets().forEach(this::populateCache);
    }

    public void rebuildCache(String orgId) {
		flush(orgId);
		populateCache(orgId);
	}

    @Override
    public void populateCache(String orgId) {
		log.info("Populating Personalmappe cache for {}", orgId);
        Event event = new Event(orgId, Constants.COMPONENT, PersonalActions.GET_ALL_PERSONALMAPPE, Constants.CACHE_SERVICE);
        consumerEventUtil.send(event);
    }


    public Optional<PersonalmappeResource> getPersonalmappeByMappeId(String orgId, String mappeId) {
        return getOne(orgId, mappeId.hashCode(),
            (resource) -> Optional
                .ofNullable(resource)
                .map(PersonalmappeResource::getMappeId)
                .map(Identifikator::getIdentifikatorverdi)
                .map(mappeId::equals)
                .orElse(false));
    }

    public Optional<PersonalmappeResource> getPersonalmappeBySystemId(String orgId, String systemId) {
        return getOne(orgId, systemId.hashCode(),
            (resource) -> Optional
                .ofNullable(resource)
                .map(PersonalmappeResource::getSystemId)
                .map(Identifikator::getIdentifikatorverdi)
                .map(systemId::equals)
                .orElse(false));
    }


	@Override
    public void onAction(Event event) {
        List<PersonalmappeResource> data;
        if (checkFintResourceCompatibility && fintResourceCompatibility.isFintResourceData(event.getData())) {
            log.info("Compatibility: Converting FintResource<PersonalmappeResource> to PersonalmappeResource ...");
            data = fintResourceCompatibility.convertResourceData(event.getData(), PersonalmappeResource.class);
        } else {
            data = objectMapper.convertValue(event.getData(), javaType);
        }
        data.forEach(resource -> {
            linker.mapLinks(resource);
            linker.resetSelfLinks(resource);
        });
        if (PersonalActions.valueOf(event.getAction()) == PersonalActions.UPDATE_PERSONALMAPPE) {
            if (event.getResponseStatus() == ResponseStatus.ACCEPTED || event.getResponseStatus() == ResponseStatus.CONFLICT) {
                List<CacheObject<PersonalmappeResource>> cacheObjects = data
                    .stream()
                    .map(i -> new CacheObject<>(i, linker.hashCodes(i)))
                    .collect(Collectors.toList());
                addCache(event.getOrgId(), cacheObjects);
                log.info("Added {} cache objects to cache for {}", cacheObjects.size(), event.getOrgId());
            } else {
                log.debug("Ignoring payload for {} with response status {}", event.getOrgId(), event.getResponseStatus());
            }
        } else {
            List<CacheObject<PersonalmappeResource>> cacheObjects = data
                    .stream()
                    .map(i -> new CacheObject<>(i, linker.hashCodes(i)))
                    .collect(Collectors.toList());
            updateCache(event.getOrgId(), cacheObjects);
            log.info("Updated cache for {} with {} cache objects", event.getOrgId(), cacheObjects.size());
        }
    }
}
