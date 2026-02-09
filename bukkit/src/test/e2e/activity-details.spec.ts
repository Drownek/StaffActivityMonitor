import { expect, test, type TestContext } from '@drownek/paper-e2e-runner';

test('activity view shows session entries with timestamps', async ({ player }: TestContext) => {
    await player.makeOp();

    await player.chat('session test message');

    await new Promise(resolve => setTimeout(resolve, 1000));

    await player.chat(`/staffactivity view ${player.username}`);

    const gui = await player.gui({ title: /Last user activity/ });

    const sessionEntry = gui.locator(i =>
        i.name.includes('clock')
    );

    await expect(sessionEntry).toHaveLore('From');
    await expect(sessionEntry).toHaveLore('To');
});

test('clicking activity entry opens detailed view', async ({ player }: TestContext) => {
    await player.makeOp();

    await player.chat('detail test message 1');
    await player.chat('/help');
    await player.chat('detail test message 2');

    await new Promise(resolve => setTimeout(resolve, 1000));

    await player.chat(`/staffactivity view ${player.username}`);

    const activityListGui = await player.gui({ title: /Last user activity/ });

    const activityEntry = activityListGui.locator(i => i.name === 'clock');
    await activityEntry.click();

    const detailsGui = await player.gui({ title: /User activity details/ });

    const messageLog = detailsGui.locator(i => i.name === 'paper');
    const commandLog = detailsGui.locator(i => i.name === 'map');

    await expect(messageLog).toHaveLore('detail test message');
    await expect(commandLog).toHaveLore('/help');
});

test('sort toggle changes activity order', async ({ player }: TestContext) => {
    await player.makeOp();
    await player.chat('sort test message');

    await new Promise(resolve => setTimeout(resolve, 1000));

    await player.chat(`/staffactivity view ${player.username}`);

    const gui = await player.gui({ title: /Last user activity/ });

    const sortToggle = gui.locator(i => i.name.includes('hopper'));

    await expect(sortToggle).toHaveLore('► Newest to oldest');

    await sortToggle.click();

    await expect(sortToggle).toHaveLore('► Oldest to newest');
    await expect(sortToggle).not.toHaveLore('► Newest to oldest');
});