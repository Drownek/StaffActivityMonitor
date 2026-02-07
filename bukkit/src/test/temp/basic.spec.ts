import { test, expect } from '../../../../01_Drafts/paper-e2e-test-framework/runner-package/runner';

test('plugin loads and handles commands', async ({ player }) => {
    await player.chat('/staffactivity reload');
    await expect(player).toHaveReceivedMessage("You don't have permission");
});

test('op player can reload plugin config', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity reload');
    await expect(player).toHaveReceivedMessage('Config reloaded');
});