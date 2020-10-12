package no.fint.consumer.models.personalmappe;

import no.fint.model.resource.arkiv.personal.PersonalmappeResource;
import no.fint.model.resource.arkiv.personal.PersonalmappeResources;
import no.fint.relations.FintLinker;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;

@Component
public class PersonalmappeLinker extends FintLinker<PersonalmappeResource> {

    public PersonalmappeLinker() {
        super(PersonalmappeResource.class);
    }

    public void mapLinks(PersonalmappeResource resource) {
        super.mapLinks(resource);
    }

    @Override
    public PersonalmappeResources toResources(Collection<PersonalmappeResource> collection) {
        return toResources(collection.stream(), 0, 0, collection.size());
    }

    @Override
    public PersonalmappeResources toResources(Stream<PersonalmappeResource> stream, int offset, int size, int totalItems) {
        PersonalmappeResources resources = new PersonalmappeResources();
        stream.map(this::toResource).forEach(resources::addResource);
        addPagination(resources, offset, size, totalItems);
        return resources;
    }

    @Override
    public String getSelfHref(PersonalmappeResource personalmappe) {
        return getAllSelfHrefs(personalmappe).findFirst().orElse(null);
    }

    @Override
    public Stream<String> getAllSelfHrefs(PersonalmappeResource personalmappe) {
        Stream.Builder<String> builder = Stream.builder();
        if (!isNull(personalmappe.getMappeId()) && !isEmpty(personalmappe.getMappeId().getIdentifikatorverdi())) {
            builder.add(createHrefWithId(personalmappe.getMappeId().getIdentifikatorverdi(), "mappeid"));
        }
        if (!isNull(personalmappe.getSystemId()) && !isEmpty(personalmappe.getSystemId().getIdentifikatorverdi())) {
            builder.add(createHrefWithId(personalmappe.getSystemId().getIdentifikatorverdi(), "systemid"));
        }
        
        return builder.build();
    }

    int[] hashCodes(PersonalmappeResource personalmappe) {
        IntStream.Builder builder = IntStream.builder();
        if (!isNull(personalmappe.getMappeId()) && !isEmpty(personalmappe.getMappeId().getIdentifikatorverdi())) {
            builder.add(personalmappe.getMappeId().getIdentifikatorverdi().hashCode());
        }
        if (!isNull(personalmappe.getSystemId()) && !isEmpty(personalmappe.getSystemId().getIdentifikatorverdi())) {
            builder.add(personalmappe.getSystemId().getIdentifikatorverdi().hashCode());
        }
        
        return builder.build().toArray();
    }

}

