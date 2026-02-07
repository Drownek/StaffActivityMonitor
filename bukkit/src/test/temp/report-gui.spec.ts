import { test, expect, type TestContext } from '../../../../01_Drafts/paper-e2e-test-framework/runner-package/runner';

test('time period selector shows all periods', async ({ player }) => {
    await player.makeOp();

    const guiPromise = player.waitForGui('Select Time Period');
    await player.chat('/staffactivity report');
    const gui = await guiPromise;

    const periods = ['Today', 'Yesterday', 'Last 7 Days', 'Last 30 Days', 'This Month', 'All Time'];

    for (const period of periods) {
        const item = gui.findItem(i => i.getDisplayName().includes(period));
        expect(item).toBeTruthy(); // Each period must exist
    }

    // Verify total count
    const allPeriodItems = gui.findAllItems(i =>
      periods.some(p => i.getDisplayName().includes(p))
    );
    expect(allPeriodItems.length).toBe(6);
});

test('clicking period opens activity report', async ({ player }) => {
    await player.makeOp();

    const selectorPromise = player.waitForGui('Select Time Period');
    await player.chat('/staffactivity report');
    const selector = await selectorPromise;

    expect(selector).toBeTruthy();

    const reportPromise = player.waitForGui('Activity Report');
    await selector.clickItem(i => i.name.includes('clock'));
    const report = await reportPromise;

    expect(report).toBeTruthy();
    expect(report.getTitle()).toContain('Activity Report');
    expect(selector.getTitle()).not.toBe(report.getTitle()); // Verify navigation occurred
});

test('report gui shows player rankings with stats', async ({ player }: TestContext) => {
    await player.makeOp();

    await player.chat('test message for report');
    await player.chat('/help');
    await new Promise(r => setTimeout(r, 1000));

    const guiPromise = player.waitForGui('Activity Report');
    await player.chat('/staffactivity top');
    const gui = await guiPromise;

    const playerEntry = gui.findItem(i =>
      i.name.includes('head') &&
      (i.hasLore('Time online') || i.hasLore('Sessions') || i.hasLore('Commands') || i.hasLore('Messages'))
    );

    if (!playerEntry) {
        throw new Error('Player entry not found in report GUI');
    }
    expect(playerEntry).toBeTruthy();

    // Verify it has at least one stat category
    const hasTimeOnline = playerEntry.hasLore('Time online');
    const hasSessions = playerEntry.hasLore('Sessions');
    const hasCommands = playerEntry.hasLore('Commands');
    const hasMessages = playerEntry.hasLore('Messages');

    expect(hasTimeOnline || hasSessions || hasCommands || hasMessages).toBe(true);
});