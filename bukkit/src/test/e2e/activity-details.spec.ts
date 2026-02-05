import {expect, type ItemWrapper, test, type TestContext} from '@drownek/paper-e2e-runner';

test('activity view shows session entries with timestamps', async ({ player }: TestContext) => {
    await player.makeOp();

    await player.chat('session test message');
    await new Promise(r => setTimeout(r, 1000));

    const guiPromise = player.waitForGui('Last user activity');
    await player.chat(`/staffactivity view ${player.username}`);
    const gui = await guiPromise;

    const sessionEntry = gui.findItem((i: ItemWrapper) => i.name.includes('clock') && i.hasLore('From'));

    if (!sessionEntry) {
        throw new Error('Session entry not found in GUI');
    }

    expect(sessionEntry).toBeTruthy();
    expect(sessionEntry.hasLore('From')).toBe(true);
    expect(sessionEntry.hasLore('To')).toBe(true);
});

test('clicking activity entry opens detailed view', async ({ player }: TestContext) => {
    await player.makeOp();

    await new Promise(r => setTimeout(r, 200));

    await player.chat('detail test message 1');
    await player.chat('/help');
    await player.chat('detail test message 2');

    await new Promise(r => setTimeout(r, 200));

    await player.chat(`/staffactivity view ${player.username}`);
    const mainGui = await player.waitForGui('Last user activity (1/1)');

    await mainGui.clickItem(i => i.name.includes('clock'));

    const detailGui = await player.waitForGui('User activity details');

    const logs = detailGui.findAllItems(i => {
        return i.name.toLowerCase().includes('paper') || i.name.toLowerCase().includes('map');
    });

    expect(logs.length).toBeGreaterThan(0);
    // Verify we have at least our test messages and command
    const hasMessage = logs.some(log =>
      log.getLore().some(line => line.includes('detail test message'))
    );
    const hasCommand = logs.some(log =>
      log.getLore().some(line => line.includes('/help'))
    );
    expect(hasMessage).toBe(true);
    expect(hasCommand).toBe(true);
});

test('sort toggle changes activity order', async ({ player }: TestContext) => {
    await player.makeOp();

    await player.chat('sort test message');

    await player.chat(`/staffactivity view ${player.username}`);
    const gui = await player.waitForGui('Last user activity (1/1)');

    const hopper = gui.findItem(i => i.name.includes('hopper'));
    if (!hopper) {
        throw new Error('Sort hopper not found in GUI');
    }
    expect(hopper).toBeTruthy();

    // 1. Validate the default state
    const initialLore = hopper.getLore().join(' ');
    expect(initialLore).toContain('► Newest to oldest');
    expect(initialLore).not.toContain('► Oldest to newest');

    // Click and wait for GUI to refresh
    await gui.clickItem(i => i.name.includes('hopper'));

    await new Promise(r => setTimeout(r, 1000));

    const updatedGui = await player.waitForGui('Last user activity (1/1)');

    const updatedHopper = updatedGui.findItem(i => i.name.includes('hopper'));

    if (!updatedHopper) {
        console.log('[DEBUG] Updated GUI items:', updatedGui.getItems().map(i => ({ name: i.name, slot: i.slot, lore: i.getLore() })));
        throw new Error('Updated sort hopper not found in GUI');
    }
    expect(updatedHopper).toBeTruthy();

    // 2. Validate the toggled state (Newest to Oldest)
    const newLore = updatedHopper.getLore().join(' ');

    expect(newLore).toContain('► Oldest to newest');
    expect(newLore).not.toContain('► Newest to oldest');
});