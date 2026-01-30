document.addEventListener('DOMContentLoaded', () => {
    const config = window.AppConfig;

    // Global Chart Defaults
    Chart.defaults.color = '#e0e0e0';
    Chart.defaults.borderColor = '#444';
    Chart.defaults.backgroundColor = '#2d2d2d';

    // --- 1. SENSOR DATA CHART ---
    if (config.sensor.exists && config.sensor.timestamps.length > 0) {
        const sensorData = config.sensor.timestamps.map((t, i) => ({
            date: new Date(t),
            pollution: config.sensor.pollution[i],
            uv: config.sensor.uv[i]
        }));

        const ctx = document.getElementById('observationChart');
        const sensorSelector = document.getElementById('timescale');

        if (ctx && sensorSelector) {
            const sensorChart = new Chart(ctx.getContext('2d'), {
                type: 'line',
                data: {
                    labels: sensorData.map(o => o.date.toLocaleString()),
                    datasets: [
                        { label: "Pollution Level", data: sensorData.map(o => o.pollution), tension: 0.3, borderColor: '#dc3545', backgroundColor: 'rgba(220, 53, 69, 0.1)', fill: true },
                        { label: "UV Index", data: sensorData.map(o => o.uv), tension: 0.3, borderColor: '#ffc107', backgroundColor: 'rgba(255, 193, 7, 0.1)', fill: true }
                    ]
                },
                options: getCommonOptions("Value")
            });

            // Listener for dropdown changes
            sensorSelector.addEventListener('change', (e) => {
                updateChart(sensorChart, sensorData, e.target.value, ['pollution', 'uv']);
            });

            // INITIAL TRIGGER: Filter based on HTML default choice on load
            updateChart(sensorChart, sensorData, sensorSelector.value, ['pollution', 'uv']);
        }
    }

    // --- 2. TRAFFIC DATA CHART ---
    if (config.traffic.exists && config.traffic.timestamps.length > 0) {
        const trafficData = config.traffic.timestamps.map((t, i) => ({
            date: new Date(t),
            congestion: config.traffic.congestion[i],
            jams: config.traffic.jams[i]
        }));

        const ctxTraffic = document.getElementById('trafficChart');
        const trafficSelector = document.getElementById('trafficTimescale');

        if (ctxTraffic && trafficSelector) {
            const trafficChart = new Chart(ctxTraffic.getContext('2d'), {
                type: 'line',
                data: {
                    labels: trafficData.map(o => o.date.toLocaleString()),
                    datasets: [
                        { label: "Congestion Level", data: trafficData.map(o => o.congestion), tension: 0.3, borderColor: '#fd7e14', backgroundColor: 'rgba(253, 126, 20, 0.1)', fill: true },
                        { label: "Traffic Jams", data: trafficData.map(o => o.jams), tension: 0.3, borderColor: '#dc3545', backgroundColor: 'rgba(220, 53, 69, 0.1)', fill: true }
                    ]
                },
                options: getCommonOptions("Count")
            });

            // Listener for dropdown changes
            trafficSelector.addEventListener('change', (e) => {
                updateChart(trafficChart, trafficData, e.target.value, ['congestion', 'jams']);
            });

            // INITIAL TRIGGER: Filter based on HTML default choice on load
            updateChart(trafficChart, trafficData, trafficSelector.value, ['congestion', 'jams']);
        }
    }

    /** * Helper: Common Chart Options
     */
    function getCommonOptions(yTitle) {
        return {
            responsive: true,
            maintainAspectRatio: false,
            scales: {
                x: {
                    grid: { display: false, drawTicks: false },
                    ticks: { display: false },
                    title: { display: true, text: "Date / Time", color: '#e0e0e0' }
                },
                y: {
                    title: { display: true, text: yTitle, color: '#e0e0e0' },
                    grid: { color: '#444' },
                    beginAtZero: true
                }
            }
        };
    }

    /** * Helper: Update Chart Data based on Timescale
     */
    function updateChart(chartInstance, fullData, timescale, keys) {
        const now = new Date();
        let cutoff;
        switch(timescale) {
            case '24h': cutoff = new Date(now - 24*60*60*1000); break;
            case '1w':  cutoff = new Date(now - 7*24*60*60*1000); break;
            case '1m':  cutoff = new Date(now - 30*24*60*60*1000); break;
            default:    cutoff = new Date(0); // All time
        }

        const filtered = fullData.filter(o => o.date >= cutoff);
        chartInstance.data.labels = filtered.map(o => o.date.toLocaleString());
        chartInstance.data.datasets[0].data = filtered.map(o => o[keys[0]]);
        chartInstance.data.datasets[1].data = filtered.map(o => o[keys[1]]);
        chartInstance.update();
    }
});