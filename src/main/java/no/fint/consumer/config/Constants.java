package no.fint.consumer.config;

public enum Constants {
;

    public static final String COMPONENT = "arkiv-personal";
    public static final String COMPONENT_CONSUMER = COMPONENT + " consumer";
    public static final String CACHE_SERVICE = "CACHE_SERVICE";

    
    public static final String CACHE_INITIALDELAY_PERSONALMAPPE = "${fint.consumer.cache.initialDelay.personalmappe:900000}";
    public static final String CACHE_FIXEDRATE_PERSONALMAPPE = "${fint.consumer.cache.fixedRate.personalmappe:900000}";
    

}
