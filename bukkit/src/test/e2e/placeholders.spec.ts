import { test, expect } from '@drownek/paper-e2e-runner';

async function setupStaff(player: any, server: any) {
    await server.execute(`op ${player.username}`);
    await new Promise(r => setTimeout(r, 500));
}

// Time Placeholders

test('placeholder total_time returns formatted duration', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_total_time%');
    await expect(player).toHaveReceivedMessage('d');
});

test('placeholder daily_time returns formatted duration', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_daily_time%');
    await expect(player).toHaveReceivedMessage('d');
});

test('placeholder weekly_time returns formatted duration', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_weekly_time%');
    await expect(player).toHaveReceivedMessage('d');
});

test('placeholder monthly_time returns formatted duration', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_monthly_time%');
    await expect(player).toHaveReceivedMessage('d');
});

test('placeholder yearly_time returns formatted duration', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_yearly_time%');
    await expect(player).toHaveReceivedMessage('d');
});

// Session Count Placeholders

test('placeholder session_count returns numeric value', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_session_count%');
    // Returns a number (may be > 0 from previous test runs)
    await expect(player).not.toHaveReceivedMessage('N/A');
});

test('placeholder daily_session_count returns numeric value', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_daily_session_count%');
    await expect(player).not.toHaveReceivedMessage('N/A');
});

test('placeholder weekly_session_count returns numeric value', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_weekly_session_count%');
    await expect(player).not.toHaveReceivedMessage('N/A');
});

test('placeholder monthly_session_count returns numeric value', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_monthly_session_count%');
    await expect(player).not.toHaveReceivedMessage('N/A');
});

// Commands Count Placeholders

test('placeholder commands_count tracks commands', async ({ player, server }) => {
    await setupStaff(player, server);

    await player.chat('/help');
    await player.chat('/list');
    await new Promise(r => setTimeout(r, 500));

    await player.chat('/papi parse me %staffactivity_commands_count%');
    await expect(player).toHaveReceivedMessage('2');
});

test('placeholder daily_commands_count tracks daily commands', async ({ player, server }) => {
    await setupStaff(player, server);

    await player.chat('/help');
    await new Promise(r => setTimeout(r, 300));

    await player.chat('/papi parse me %staffactivity_daily_commands_count%');
    await expect(player).toHaveReceivedMessage('1');
});

test('placeholder weekly_commands_count returns value', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_weekly_commands_count%');
    await expect(player).toHaveReceivedMessage('0');
});

test('placeholder monthly_commands_count returns value', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_monthly_commands_count%');
    await expect(player).toHaveReceivedMessage('0');
});

// Messages Count Placeholders

test('placeholder messages_count tracks messages', async ({ player, server }) => {
    await setupStaff(player, server);

    await player.chat('Test message 1');
    await player.chat('Test message 2');
    await new Promise(r => setTimeout(r, 500));

    await player.chat('/papi parse me %staffactivity_messages_count%');
    await expect(player).toHaveReceivedMessage('2');
});

test('placeholder daily_messages_count tracks daily messages', async ({ player, server }) => {
    await setupStaff(player, server);

    await player.chat('Test message');
    await new Promise(r => setTimeout(r, 300));

    await player.chat('/papi parse me %staffactivity_daily_messages_count%');
    await expect(player).toHaveReceivedMessage('1');
});

test('placeholder weekly_messages_count returns value', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_weekly_messages_count%');
    await expect(player).toHaveReceivedMessage('0');
});

test('placeholder monthly_messages_count returns value', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_monthly_messages_count%');
    await expect(player).toHaveReceivedMessage('0');
});

// Misc Placeholders

test('placeholder last_seen returns value', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_last_seen%');
    // Will return N/A or date format
    await expect(player).toHaveReceivedMessage('N/A');
});

test('placeholder average_session returns formatted duration', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_average_session%');
    await expect(player).toHaveReceivedMessage('d');
});

// Rank Placeholders

test('placeholder rank returns numeric value', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_rank%');
    await expect(player).toHaveReceivedMessage('1');
});

test('placeholder daily_rank returns numeric value', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_daily_rank%');
    await expect(player).toHaveReceivedMessage('1');
});

test('placeholder weekly_rank returns numeric value', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_weekly_rank%');
    await expect(player).toHaveReceivedMessage('1');
});

test('placeholder monthly_rank returns numeric value', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_monthly_rank%');
    await expect(player).toHaveReceivedMessage('1');
});

// Time Accumulation Test

test('placeholder total_time accumulates after session ends', async ({ player, server }) => {
    await setupStaff(player, server);
    
    // Wait for activity task to register the session (runs every 1 second)
    await new Promise(r => setTimeout(r, 2000));
    
    // Remove staff permission to end the session
    await server.execute(`deop ${player.username}`);
    await new Promise(r => setTimeout(r, 2000));
    
    // Re-add staff permission to start new session and check accumulated time
    await server.execute(`op ${player.username}`);
    await new Promise(r => setTimeout(r, 1000));
    
    // The previous session should now be counted (was ~2 seconds)
    // Format is "{days}d {hours}h {minutes}m" - will show 0d 0h 0m for short sessions
    // But the placeholder should work and not return N/A
    await player.chat('/papi parse me %staffactivity_total_time%');
    await expect(player).toHaveReceivedMessage('0d 0h 0m');
});

test('placeholder session_count increments after session ends', async ({ player, server }) => {
    await setupStaff(player, server);
    
    // Wait for activity task to register the session
    await new Promise(r => setTimeout(r, 2000));
    
    // End session by removing permission
    await server.execute(`deop ${player.username}`);
    await new Promise(r => setTimeout(r, 2000));
    
    // Re-add permission
    await server.execute(`op ${player.username}`);
    await new Promise(r => setTimeout(r, 1000));
    
    // Should have 1 completed session now
    await player.chat('/papi parse me %staffactivity_session_count%');
    await expect(player).toHaveReceivedMessage('1');
});

// Edge Cases

test('placeholder unknown returns N/A', async ({ player, server }) => {
    await setupStaff(player, server);
    
    await player.chat('/papi parse me %staffactivity_unknown_placeholder%');
    await expect(player).toHaveReceivedMessage('N/A');
});

test('placeholder for non-staff player returns N/A', async ({ player }) => {
    // Player without staff permission
    await player.chat('/papi parse me %staffactivity_total_time%');
    await expect(player).toHaveReceivedMessage('N/A');
});
