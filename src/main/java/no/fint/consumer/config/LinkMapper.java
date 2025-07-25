package no.fint.consumer.config;

import no.fint.consumer.utils.RestEndpoints;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import no.fint.model.arkiv.personal.Personalmappe;

public class LinkMapper {

    public static Map<String, String> linkMapper(String contextPath) {
        return ImmutableMap.<String,String>builder()
            .put(Personalmappe.class.getName(), contextPath + RestEndpoints.PERSONALMAPPE)
            .put("no.fint.model.felles.kodeverk.iso.Landkode", "/felles/kodeverk/iso/landkode")
            .put("no.fint.model.arkiv.kodeverk.DokumentStatus", "/arkiv/kodeverk/dokumentstatus")
            .put("no.fint.model.arkiv.kodeverk.DokumentType", "/arkiv/kodeverk/dokumenttype")
            .put("no.fint.model.arkiv.kodeverk.TilknyttetRegistreringSom", "/arkiv/kodeverk/tilknyttetregistreringsom")
            .put("no.fint.model.arkiv.noark.Arkivressurs", "/arkiv/noark/arkivressurs")
            .put("no.fint.model.arkiv.kodeverk.Format", "/arkiv/kodeverk/format")
            .put("no.fint.model.arkiv.kodeverk.Variantformat", "/arkiv/kodeverk/variantformat")
            .put("no.fint.model.arkiv.noark.Dokumentfil", "/arkiv/noark/dokumentfil")
            .put("no.fint.model.arkiv.kodeverk.JournalpostType", "/arkiv/kodeverk/journalposttype")
            .put("no.fint.model.arkiv.kodeverk.JournalStatus", "/arkiv/kodeverk/journalstatus")
            .put("no.fint.model.arkiv.noark.AdministrativEnhet", "/arkiv/noark/administrativenhet")
            .put("no.fint.model.arkiv.kodeverk.Tilgangsgruppe", "/arkiv/kodeverk/tilgangsgruppe")
            .put("no.fint.model.arkiv.noark.Arkivdel", "/arkiv/noark/arkivdel")
            .put("no.fint.model.arkiv.noark.Klassifikasjonssystem", "/arkiv/noark/klassifikasjonssystem")
            .put("no.fint.model.arkiv.kodeverk.KorrespondansepartType", "/arkiv/kodeverk/korrespondanseparttype")
            .put("no.fint.model.arkiv.kodeverk.Merknadstype", "/arkiv/kodeverk/merknadstype")
            .put("no.fint.model.arkiv.kodeverk.PartRolle", "/arkiv/kodeverk/partrolle")
            .put("no.fint.model.felles.Person", "/felles/person")
            .put("no.fint.model.administrasjon.personal.Personalressurs", "/administrasjon/personal/personalressurs")
            .put("no.fint.model.administrasjon.organisasjon.Organisasjonselement", "/administrasjon/organisasjon/organisasjonselement")
            .put("no.fint.model.arkiv.kodeverk.Saksmappetype", "/arkiv/kodeverk/saksmappetype")
            .put("no.fint.model.arkiv.kodeverk.Saksstatus", "/arkiv/kodeverk/saksstatus")
            .put("no.fint.model.arkiv.kodeverk.Skjermingshjemmel", "/arkiv/kodeverk/skjermingshjemmel")
            .put("no.fint.model.arkiv.kodeverk.Tilgangsrestriksjon", "/arkiv/kodeverk/tilgangsrestriksjon")
            /* .put(TODO,TODO) */
            .build();
    }

}
