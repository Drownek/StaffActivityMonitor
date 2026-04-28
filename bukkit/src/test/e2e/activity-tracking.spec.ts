import {expect, test, type TestContext} from '@drownek/paper-e2e-runner';

test('activity tracking records player messages', async ({ player }: TestContext) => {
    await player.makeOp();

    await player.chat('Hello this is a test message');
    await player.chat('Another test message for tracking');

    await new Promise(resolve => setTimeout(resolve, 1000));

    await player.chat(`/staffactivity view ${player.username}`);
    const gui = await player.gui({ title: /Last user activity \(/ });

    const messageItem = gui.locator(i => i.name === 'clock');
    
    await expect(messageItem).toHaveLore('messages');
    
    const lore = messageItem.loreText();
    const messageCount = parseInt(lore.match(/(\d+)\s+messages/)?.[1] || '0');
    expect(messageCount).toBe(2);
});

test('activity tracking records player commands', async ({ player }: TestContext) => {
    await player.makeOp();

    await player.chat('/help');
    await player.chat('/list');

    await new Promise(resolve => setTimeout(resolve, 1000));

    await player.chat(`/staffactivity view ${player.username}`);
    const gui = await player.gui({ title: /Last user activity \(/ });

    const commandItem = gui.locator(i => i.hasLore('commands'));
    
    await expect(commandItem).toHaveLore('commands');
    
    const lore = commandItem.loreText();
    const commandCount = parseInt(lore.match(/(\d+)\s+commands/)?.[1] || '0');
    expect(commandCount).toBeGreaterThanOrEqual(2);
    expect(commandCount).toBeLessThanOrEqual(3);
});

test('activity tracking ignores configured commands', async ({ player }: TestContext) => {
    await player.makeOp();

    await player.chat('/help');
    await player.chat('/login secretpassword');
    await player.chat('/register secretpassword secretpassword');

    await new Promise(resolve => setTimeout(resolve, 1000));

    await player.chat(`/staffactivity view ${player.username}`);
    const gui = await player.gui({ title: /Last user activity/ });

    const commandItem = gui.locator(i => i.hasLore('commands'));

    await expect(commandItem).toHaveLore('commands');

    const lore = commandItem.loreText();
    const commandCount = parseInt(lore.match(/(\d+)\s+commands/)?.[1] || '0');
    expect(commandCount).toBeGreaterThanOrEqual(1);
    expect(commandCount).toBeLessThanOrEqual(2);
});