import {expect, test, type TestContext} from '@drownek/paper-e2e-runner';

test('view command opens staff activity list', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity view');

    const gui = await player.gui({ title: /Staff activity/ });

    expect(gui).toBeDefined();
});

test('report command opens time period selector', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity report');
    const gui = await player.gui({ title: /Select Time Period/ });

    expect(gui).toBeDefined();
    
    const periodOption = gui.locator(i => i.name.includes('clock'));

    expect(periodOption).toBeDefined();
});

test('top command opens activity report for all time', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity top');

    const gui = await player.gui({ title: /Activity Report/ });

    expect(gui).toBeDefined();
});

test('report with period argument opens report directly', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity report today');
    const gui = await player.gui({ title: /Activity Report/ });

    expect(gui).toBeDefined();
});

test('invalid time period shows error message', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity report invalid-period');
    await expect(player).toHaveReceivedMessage('Invalid time period');
});

test('view command with player name opens player activity', async ({ player }: TestContext) => {
    await player.makeOp();

    await player.chat('test activity message');

    await player.chat(`/staffactivity view ${player.username}`);
    const gui = await player.gui({ title: /Last user activity/ });

    expect(gui.title).toBeDefined();
    
    const activityEntry = gui.locator(i => i.name.includes('clock'));
    expect(activityEntry).toBeTruthy();
});