package us.vchain.jvcn.maven;

import org.apache.maven.shared.dependency.graph.DependencyNode;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.lang.String.format;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toMap;

class MavenUtils {
    private MavenUtils() {

    }

    static Set<DependencyNode> transitiveClosure(final Set<DependencyNode> dependencies) {
        if (dependencies == null || dependencies.isEmpty()) {
            return emptySet();
        }
        final Set<DependencyNode> result = new LinkedHashSet<>();
        for (final DependencyNode dependency : dependencies) {
            result.addAll(dependencies);
            result.addAll(transitiveClosure(new LinkedHashSet<>(dependency.getChildren())));
        }
        return result;
    }

    static boolean asBoolean(final String value) {
        switch (value.toLowerCase()) {
            case "true":
                return true;
            case "false":
                return false;
            default:
                throw new IllegalArgumentException("No boolean value: " + value);
        }
    }

    static <T, K, U> Collector<T, ?, Map<K, U>> toLinkedMap(
        final Function<? super T, ? extends K> keyMapper,
        final Function<? super T, ? extends U> valueMapper) {
        return toMap(
            keyMapper,
            valueMapper,
            (u, v) -> {
                throw new IllegalStateException(format("Duplicate key %s", u));
            },
            LinkedHashMap::new);
    }
}
