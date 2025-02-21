package com.mohistmc.banner.mixin.network.chat;

import com.google.common.collect.Streams;
import com.mohistmc.banner.injection.network.chat.InjectionComponent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Mixin(Component.class)
public interface MixinComponent extends Iterable<Component>, InjectionComponent {

    // @formatter:off
    @Shadow List<Component> getSiblings();
    // @formatter:on

    default Stream<Component> stream() {
        class Func implements Function<Component, Stream<? extends Component>> {

            @Override
            public Stream<? extends Component> apply(Component component) {
                return ((InjectionComponent) component).bridge$stream();
            }
        }
        return Streams.concat(Stream.of((Component) this), this.getSiblings().stream().flatMap(new Func()));
    }

    @Override
    default @NotNull Iterator<Component> iterator() {
        return this.stream().iterator();
    }

    @Override
    default Stream<Component> bridge$stream() {
        return stream();
    }
}
