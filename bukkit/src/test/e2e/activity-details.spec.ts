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

    await new Promise(r => setTimeout(r, 1000));

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
    await player.makeOp();

    await player.chat('sort test message');

    await new Promise(r => setTimeout(r, 1000));

    await player.chat(`/staffactivity view ${player.username}`);
    await player.waitForGui(gui =>
        gui.title.includes('Last user activity') &&
        gui.hasItem(i => i.name.includes('hopper'))
    );

    const hopper = player.getCurrentGui()!.findItem(i => i.name.includes('hopper'))!;

    const initialLore = hopper.getLore().join(' ');
    expect(initialLore).toContain('► Newest to oldest');
    expect(initialLore).not.toContain('► Oldest to newest');

    await player.clickGuiItem(i => i.name.includes('hopper'));

    await player.waitForGui(gui => gui.title.includes('Last user activity') && gui.hasItem(i => i.name.includes('hopper')));

    console.log("waited done, title is: " + player.getCurrentGui()!.title + "")

    const updatedHopper = player.getCurrentGui()!.findItem(i => i.name.includes('hopper'))!;

    // 2. Validate the toggled state (Newest to Oldest)
    const newLore = updatedHopper.getLore().join(' ');

    expect(newLore).toContain('► Oldest to newest');
    expect(newLore).not.toContain('► Newest to oldest');
});