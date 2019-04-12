/*
 * Copyright (c) 2018, Lotto <https://github.com/devLotto>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.mixins;

import net.runelite.api.MenuAction;
import net.runelite.api.World;
import net.runelite.api.PrayerName;
import net.runelite.api.mixins.Inject;
import net.runelite.api.mixins.Mixin;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.rs.api.RSClient;

@Mixin(RSClient.class)
public abstract class PrayerActivationMixin implements RSClient
{
	@Inject
	@Override
	public void openPrayerMenu()
	{
		System.out.println("Opening prayer menu");
		// TODO: Also handle FIXED_VIEWPORT_PRAYER_TAB
		// The clicked x & y coordinates (the last arguments) are not processed in the game or sent to Jagex, so they don't have to be real.
		menuAction(-1, WidgetInfo.RESIZABLE_VIEWPORT_BOTTOM_LINE_PRAYER_TAB.getId(), MenuAction.WIDGET_DEFAULT.getId(), 1, "Prayer", "", 658, 384);
	}

	@Inject
	@Override
	public void activatePrayer(String widgetInfoName)
	{
		System.out.println("Activating prayer: " + widgetInfoName);
		String prayerName = PrayerName.valueOf(widgetInfoName).getName();
		System.out.println(prayerName);
		String fullWidgetInfoName = "PRAYER_" + widgetInfoName;
		System.out.println(fullWidgetInfoName);
		// The clicked x & y coordinates (the last arguments) are not processed in the game or sent to Jagex, so they don't have to be real.
		menuAction(-1, WidgetInfo.valueOf(fullWidgetInfoName).getId(), MenuAction.WIDGET_DEFAULT.getId(), 1, "Activate", "<col=ff9040>" + prayerName + "</col>", 683, 244);
	}
}
