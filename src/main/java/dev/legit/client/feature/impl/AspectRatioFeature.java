package dev.legit.client.feature.impl;

import dev.legit.client.feature.Category;
import dev.legit.client.feature.Feature;
import dev.legit.client.feature.setting.NumSetting;

/** «Растяг»: меняет соотношение сторон картинки мира (интерфейс не трогает). 1.33 = 4:3, 1.25 = 5:4, 1.78 = 16:9. */
public class AspectRatioFeature extends Feature {
	public final NumSetting ratio = add(new NumSetting("Ratio", 1.33, 0.8, 2.4, 0.01, ""));

	public AspectRatioFeature() {
		super("Aspect Ratio", "Stretched resolution (4:3 = 1.33, 5:4 = 1.25)", Category.VISUAL);
	}
}
