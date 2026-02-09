import { expect, test, type TestContext } from '@drownek/paper-e2e-runner';

test('time period selector shows all periods', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity report');
    const gui = await player.gui({ title: /Select Time Period/ });

    const periods = ['Today', 'Yesterday', 'Last 7 Days', 'Last 30 Days', 'This Month', 'All Time'];

    for (const period of periods) {
        const periodItem = gui.locator(i => i.getDisplayName().includes(period));
        const displayName = periodItem.displayName();
        expect(displayName).toContain(period);
    }
});

test('clicking period opens activity report', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity report');
    const selectorGui = await player.gui({ title: /Select Time Period/ });

    const periodOption = selectorGui.locator(i => i.name.includes('clock'));
    await periodOption.click();

    const reportGui = await player.gui({ title: /Activity Report/ });

    expect(reportGui.title).toContain('Activity Report');
    expect(selectorGui.title).not.toBe(reportGui.title);
});

test('report gui shows player rankings with stats', async ({ player }: TestContext) => {
    await player.makeOp();

    await player.chat('test message for report');
    await player.chat('/help');

    await player.chat('/staffactivity top');
    const gui = await player.gui({ title: /Activity Report/ });

    // Locate player entry in rankings
    const playerEntry = gui.locator(i =>
        i.name.includes('head') &&
        (i.hasLore('Time online') ||
            i.hasLore('Sessions') ||
            i.hasLore('Commands') ||
            i.hasLore('Messages'))
    );

    // Verify at least one stat category exists
    const lore = playerEntry.loreText();
    const hasStats = lore.includes('Time online') ||
        lore.includes('Sessions') ||
        lore.includes('Commands') ||
        lore.includes('Messages');

    expect(hasStats).toBe(true);
});