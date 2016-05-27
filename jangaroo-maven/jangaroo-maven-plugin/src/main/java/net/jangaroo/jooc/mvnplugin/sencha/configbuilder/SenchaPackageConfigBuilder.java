package net.jangaroo.jooc.mvnplugin.sencha.configbuilder;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.jangaroo.jooc.mvnplugin.Type;
import net.jangaroo.jooc.mvnplugin.sencha.SenchaUtils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Builder for package.json.
 */
public class SenchaPackageConfigBuilder extends SenchaPackageOrAppConfigBuilder<SenchaPackageConfigBuilder> {

  static final String EXTEND = "extend";

  public SenchaPackageConfigBuilder extend(String theme) {
    return nameValue(EXTEND, theme);
  }

  @Nonnull
  @Override
  public Map<String, Object> build() {
    if (Type.CODE.equals(config.get(TYPE)) || (Type.THEME.equals(config.get(TYPE)))) {
      // modify resources to circumvent sencha bug
      updateResourcesForWorkaround(JS);
      updateResourcesForWorkaround(CSS);
    }

    return super.build();
  }

  private void updateResourcesForWorkaround(String resourceType) {
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> resources = (List<Map<String, Object>>) config.get(resourceType);
    if (resources != null) {
      List<Map<String, Object>> resourcesForWorkaround = getResourcesForWorkaround(resources);
      if (!resourcesForWorkaround.isEmpty()) {
        List<Map<String, Object>> updatedResources = Lists.newArrayList(Iterables.filter(resources, Predicates.not(Predicates.in(resourcesForWorkaround))));

        config.put(resourceType, updatedResources);

        addToProfile(SenchaUtils.DEVELOPMENT_PROFILE, resourceType, resourcesForWorkaround);
        addToProfile(SenchaUtils.TESTING_PROFILE, resourceType, resourcesForWorkaround);
        addToProfile(SenchaUtils.PRODUCTION_PROFILE, resourceType, getConvertedResourcesForProduction(resourcesForWorkaround));
      }
    }
  }

  private static List<Map<String, Object>> getResourcesForWorkaround(List<Map<String, Object>> jsResources) {
    List<Map<String, Object>> resourcesForWorkaround = new ArrayList<>();
    for (Map<String, Object> resourcesEntity: jsResources) {
      if (useForSenchaWorkaround(resourcesEntity)) {
        resourcesForWorkaround.add(resourcesEntity);
      }
    }
    return resourcesForWorkaround;
  }

  private void addToProfile(String profile, String type, List<Map<String, Object>> resources) {
    if (resources != null && !resources.isEmpty()) {
      profile(profile, ImmutableMap.<String, Object>of(type, resources));
    }
  }

  private static List<Map<String, Object>> getConvertedResourcesForProduction(List<Map<String, Object>> resources) {
    ArrayList<Map<String, Object>> convertedResources = new ArrayList<>(resources.size());
    for (Map<String, Object> resourcesEntity: resources) {
      String path = (String) resourcesEntity.get(PATH);
      String modifiedPath = path.replaceAll("^resources/", "resources/\\${package.name}/");

      HashMap<String, Object> modifiedResourcesEntitiy = new HashMap<>();
      modifiedResourcesEntitiy.putAll(resourcesEntity);
      modifiedResourcesEntitiy.put(PATH, modifiedPath);
      convertedResources.add(modifiedResourcesEntitiy);
    }

    return convertedResources;
  }

  private static boolean useForSenchaWorkaround(Map<String, Object> resourceEntity) {
    boolean includeInBundle = getAsBoolean(resourceEntity.get(INCLUDE_IN_BUNDLE));
    boolean bundle = getAsBoolean(resourceEntity.get(BUNDLE));

    return !includeInBundle && !bundle;
  }

  private static boolean getAsBoolean(Object value) {
    return value == null || Boolean.parseBoolean(value.toString());
  }
}
