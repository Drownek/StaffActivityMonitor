import { test, expect } from '@drownek/paper-e2e-runner';

test('plugin loads and handles commands', async ({ player }) => {
    await player.chat('/staffactivity reload');
    await expect(player).toHaveReceivedMessage("You don't have permission");
});

test('op player can reload plugin config', async ({ player, server }) => {
    await server.execute(`op ${player.username}`);

    await player.chat('/staffactivity reload');
    await expect(player).toHaveReceivedMessage('Config reloaded');
});