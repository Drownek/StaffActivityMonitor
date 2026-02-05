import { test, expect, type TestContext } from '@drownek/paper-e2e-runner';

test('activity tracking records player messages', async ({ player }: TestContext) => {
    await player.makeOp();

    await player.chat('Hello this is a test message');
    await player.chat('Another test message for tracking');

    const guiPromise = player.waitForGui('Last user activity');
    await player.chat(`/staffactivity view ${player.username}`);
    const gui = await guiPromise;

    const messageItem = gui.findItem(i => i.hasLore('messages'));

    if (!messageItem) {
        throw new Error('Message item not found in GUI');
    }
    expect(messageItem).toBeTruthy();
    expect(messageItem.hasLore('messages')).toBe(true);

    // Verify the count includes our messages
    const lore = messageItem.getLore().join(' ');
    const messageCount = parseInt(lore.match(/(\d+)\s+messages?/)?.[1] || '0');
    expect(messageCount).toBeGreaterThanOrEqual(2);
});
test('activity tracking records player commands', async ({ player }: TestContext) => {
    await player.makeOp();

    await player.chat('/help');
    await player.chat('/list');

    const guiPromise = player.waitForGui('Last user activity');
    await player.chat(`/staffactivity view ${player.username}`);
    const gui = await guiPromise;

    const commandItem = gui.findItem(i => i.hasLore('commands'));

    if (!commandItem) {
        throw new Error('Command item not found in GUI');
    }
    expect(commandItem).toBeTruthy();
    expect(commandItem.hasLore('commands')).toBe(true);

    // Verify the count includes our commands
    const lore = commandItem.getLore().join(' ');
    const commandCount = parseInt(lore.match(/(\d+)\s+commands?/)?.[1] || '0');
    expect(commandCount).toBeGreaterThanOrEqual(2);
});