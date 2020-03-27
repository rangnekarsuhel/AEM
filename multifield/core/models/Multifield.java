package com.aem.traning.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
 
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.settings.SlingSettingsService;
 
@Model(adaptables = Resource.class)
public class Multifield {
 
    // Inject the products node under the current node
    @Inject
    @Optional
    public Resource multifield;

}