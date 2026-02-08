import {expect, test, type TestContext} from '@drownek/paper-e2e-runner';

test('activity view shows session entries with timestamps', async ({ player }: TestContext) => {
    await player.makeOp();

    await player.chat('session test message');

    await new Promise(r => setTimeout(r, 1000));

    await player.chat(`/staffactivity view ${player.username}`);

    await player.waitForGui(gui => gui.title.includes('Last user activity'));

    const sessionEntry = await player.waitForGuiItem(i =>
        i.name.includes('clock') && i.hasLore('From')
    );

    expect(sessionEntry).toBeTruthy();
    expect(sessionEntry.hasLore('From')).toBe(true);
    expect(sessionEntry.hasLore('To')).toBe(true);
});

test('clicking activity entry opens detailed view', async ({ player }: TestContext) => {
    await player.makeOp();

    await player.chat('detail test message 1');
    await player.chat('/help');
    await player.chat('detail test message 2');

    await new Promise(r => setTimeout(r, 5000));

    await player.chat(`/staffactivity view ${player.username}`);

    await player.waitForGui(gui =>
        gui.title.includes('Last user activity') && gui.hasItem(i => i.name === 'clock')
    );

    await player.clickGuiItem(i => i.name === 'clock');

    const gui = await player.waitForGui(gui =>
        gui.title.includes('User activity details') &&
        gui.hasItem(i => i.name === 'paper') &&
        gui.hasItem(i => i.name === 'map')
    );

    const logs = gui.findAllItems(i => i.name === 'paper' || i.name === 'map');

    expect(logs.length).toBeGreaterThan(0);

    const hasMessage = logs.some(log => log.hasLore('detail test message'));
    const hasCommand = logs.some(log => log.hasLore('/help'));
    expect(hasMessage).toBe(true);
    expect(hasCommand).toBe(true);
});

test('sort toggle changes activity order', async ({ player }: TestContext) => {
    // Setup: Give player permissions and create activity
    await player.makeOp();
    await player.chat('sort test message');
    await new Promise(r => setTimeout(r, 1000));

    // Open the staff activity GUI
    await player.chat(`/staffactivity view ${player.username}`);

    // Get a live handle to the GUI (waits only for title match, NOT for items/lore)
    const gui = await player.gui({
        title: /Last user activity/
    });

    // Create a locator (NOT a snapshot - re-evaluates each time it's used)
    const sortToggle = gui.locator(i => i.name.includes('hopper'));

    // Assertion with automatic retry - waits for lore to appear/update
    await expect(sortToggle).toHaveLore('► Newest to oldest');

    // Click also retries until the item exists
    await sortToggle.click();

    // Same locator, same variable - but the GUI state has changed
    // The expectation polls until the new lore appears
    await expect(sortToggle).toHaveLore('► Oldest to newest');

    // Negative assertion also retries (waits for old lore to disappear)
    await expect(sortToggle).not.toHaveLore('► Newest to oldest');
});