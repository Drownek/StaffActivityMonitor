import {expect, test, type TestContext} from '@drownek/paper-e2e-runner';

test('view command opens staff activity list', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity view');
    const gui = await player.waitForGui(g => g.title.includes('Staff activity'));

    expect(gui).toBeTruthy();
    expect(gui.title).toContain('Staff activity');
    // Verify it has navigation controls
    const hasControls = gui.findItem(i => i.name.includes('arrow') || i.name.includes('barrier'));
    expect(hasControls).toBeTruthy();
});

test('report command opens time period selector', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity report');
    const gui = await player.waitForGui(g => g.title.includes('Select Time Period'));

    expect(gui).toBeTruthy();
    expect(gui.title).toBe('Select Time Period');
    // Verify it has period options
    const periods = gui.findAllItems(i => i.name.includes('clock'));
    expect(periods.length).toBeGreaterThan(0);
});

test('top command opens activity report for all time', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity top');
    const gui = await player.waitForGui(g => g.title.includes('Activity Report'));

    expect(gui).toBeTruthy();
    expect(gui.title).toContain('Activity Report');
});

test('report with period argument opens report directly', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity report today');
    const gui = await player.waitForGui(g => g.title.includes('Activity Report'));

    expect(gui).toBeTruthy();
    expect(gui.title).toContain('Activity Report');
    // Verify the period is reflected in the GUI or data
});

test('invalid time period shows error message', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity report invalid-period');
    await expect(player).toHaveReceivedMessage('Invalid time period');
});

test('view command with player name opens player activity', async ({ player }: TestContext) => {
    await player.makeOp();

    await player.chat('test activity message');
    await new Promise(r => setTimeout(r, 500));

    await player.chat(`/staffactivity view ${player.username}`);
    const gui = await player.waitForGui(g => g.title.includes('Last user activity'));

    expect(gui).toBeTruthy();
    expect(gui.title).toContain('Last user activity');
    // Verify it shows activity for the specified player
    const activityEntry = gui.findItem(i => i.name.includes('clock'));
    expect(activityEntry).toBeTruthy();
});