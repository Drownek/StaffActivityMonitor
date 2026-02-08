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
    // --- Test setup ---------------------------------------------------------
    await player.makeOp();

    // Produce multiple types of activity: chat messages and commands
    await player.chat('detail test message 1');
    await player.chat('/help');
    await player.chat('detail test message 2');

    // NOTE: Intentional sleep to ensure activity is logged server-side
    await new Promise(r => setTimeout(r, 5000));

    // Open the staff activity GUI
    await player.chat(`/staffactivity view ${player.username}`);

    // --- First GUI context --------------------------------------------------
    // `player.gui()` waits ONLY until a GUI with matching title exists.
    // The returned handle is LIVE - always reflects current GUI state.
    const activityListGui = await player.gui({
        title: /Last user activity/
    });

    // --- Locator for activity entry -----------------------------------------
    // This locator represents \"the first clock item in the current GUI\".
    // It will be re-evaluated each time it's used.
    const activityEntry = activityListGui.locator(i => i.name === 'clock');

    // --- Interaction --------------------------------------------------------
    // Click the activity entry to open detailed view.
    // This retries until the clock exists, then clicks it.
    await activityEntry.click();

    // --- Second GUI context -------------------------------------------------
    // Wait for the detailed view GUI to open.
    // This is a NEW GUI, so we get a new live handle.
    const detailsGui = await player.gui({
        title: /User activity details/
    });

    // --- Locators for specific log entries ----------------------------------
    // Create locators for log entries containing specific content.
    // Each locator represents \"the first paper/map item with this lore\".

    const messageLog = detailsGui.locator(i =>
        (i.name === 'paper' || i.name === 'map') &&
        i.getLore().join(' ').includes('detail test message')
    );

    const commandLog = detailsGui.locator(i =>
        (i.name === 'paper' || i.name === 'map') &&
        i.getLore().join(' ').includes('/help')
    );

    // --- Assertions ---------------------------------------------------------
    // These expectations RETRY until the conditions are met.
    //
    // Each assertion:
    // - Keeps re-finding the item using the locator
    // - Checks if the lore matches
    // - Waits until true or times out
    //
    // This handles:
    // - GUI opening with empty slots
    // - Items appearing asynchronously
    // - Lore being set after items appear
    await expect(messageLog)
        .toHaveLore('detail test message');

    await expect(commandLog)
        .toHaveLore('/help');
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