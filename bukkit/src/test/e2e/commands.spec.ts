import {expect, test} from "@drownek/paper-e2e-runner";

test('commands require staffactivity.commands permission', async ({ player }) => {
    await player.chat('/staffactivity reload');
    await expect(player).toHaveReceivedMessage("You don't have permission");
});

test('reload command reloads config with permission', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity reload');
    await expect(player).toHaveReceivedMessage('Config reloaded');
});

test('unknown subcommand shows help or error', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity unknowncommand');

    await expect(player).toHaveReceivedMessage('Invalid command usage');
    await expect(player).toHaveReceivedMessage('/staffactivity reload');
    await expect(player).toHaveReceivedMessage('/staffactivity view');
});

test('export command works with permission', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity export');

    await expect(player).toHaveReceivedMessage('Activity report exported to');
    await expect(player).toHaveReceivedMessage('.csv');
});

test('export command accepts time period argument', async ({ player }) => {
    await player.makeOp();

    await player.chat('/staffactivity export today');

    await expect(player).toHaveReceivedMessage('Activity report exported to');
    await expect(player).toHaveReceivedMessage('today');
    await expect(player).toHaveReceivedMessage('.csv');
});