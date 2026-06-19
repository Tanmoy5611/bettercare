// Hide the top message after a short time so it does not stay on the screen
setTimeout(() => {
    const banner = document.getElementById('notification-banner');
    if (banner) banner.style.display = 'none';
}, 15000);

function hideBanner() {
    const banner = document.getElementById('notification-banner');
    if (banner) banner.style.display = 'none';
}

function setPollutionClass() {
    // Pick the colour class that matches the pollution value
    const pollutionCard = document.getElementById('pollution-card');
    if (pollutionCard) {
        const level = Number(pollutionCard.getAttribute('data-pollution'));
        if (!Number.isFinite(level)) return;
        pollutionCard.classList.remove('pollution-good', 'pollution-moderate', 'pollution-unhealthy-sensitive', 'pollution-unhealthy', 'pollution-very-unhealthy', 'pollution-hazardous');
        let className = 'pollution-good';
        
        if (level <= 50) {
            className = 'pollution-good';
        } else if (level <= 100) {
            className = 'pollution-moderate';
        } else if (level <= 150) {
            className = 'pollution-unhealthy-sensitive';
        } else if (level <= 200) {
            className = 'pollution-unhealthy';
        } else if (level <= 300) {
            className = 'pollution-very-unhealthy';
        } else {
            className = 'pollution-hazardous';
        }
        
        pollutionCard.classList.add(className);
    }
}

function setUvClass() {
    const uvCard = document.getElementById('uv-card');
    if (uvCard) {
        const index = Number(uvCard.getAttribute('data-uv'));
        if (!Number.isFinite(index)) return;
        uvCard.classList.remove('uv-low', 'uv-moderate', 'uv-high', 'uv-very-high');
        let className = 'uv-low';
        
        if (index >= 1 && index <= 2) {
            className = 'uv-low';
        } else if (index >= 3 && index <= 5) {
            className = 'uv-moderate';
        } else if (index >= 6 && index <= 7) {
            className = 'uv-high';
        } else if (index >= 8) {
            className = 'uv-very-high';
        }
        
        uvCard.classList.add(className);
    }
}

function setNotificationBannerClass() {
    const banner = document.getElementById('notification-banner');
    if (banner) {
        const dangerLevel = banner.getAttribute('data-danger-level');
        if (dangerLevel) {
            let className = 'banner-good';
            switch(dangerLevel) {
                case 'hazardous':
                    className = 'banner-hazardous';
                    break;
                case 'very-unhealthy':
                    className = 'banner-very-unhealthy';
                    break;
                case 'unhealthy':
                    className = 'banner-unhealthy';
                    break;
                case 'unhealthy-sensitive':
                    className = 'banner-unhealthy-sensitive';
                    break;
                case 'moderate':
                    className = 'banner-moderate';
                    break;
                default:
                    className = 'banner-good';
            }
            banner.classList.add(className);
        } else {
            banner.classList.add('banner-info');
        }
    }
}

function setFuturePollutionClass() {
    const pollutionCard = document.getElementById('future-pollution-card');
    if (pollutionCard) {
        const future = Number(pollutionCard.getAttribute('data-future'));
        const current = Number(pollutionCard.getAttribute('data-current'));
        if (!Number.isFinite(future) || !Number.isFinite(current)) return;
        pollutionCard.classList.remove('pollution-good', 'pollution-moderate', 'pollution-unhealthy-sensitive', 'pollution-unhealthy', 'pollution-very-unhealthy', 'pollution-hazardous');

        let className = '';

        if (future < current) {
            className = 'pollution-good';
        } else if (future === current) {
            className = 'pollution-moderate';
        } else {
            className = 'pollution-unhealthy';
        }

        pollutionCard.classList.add(className);
    }
}


document.addEventListener('DOMContentLoaded', function() {
    setPollutionClass();
    setFuturePollutionClass();
    setUvClass();
    setNotificationBannerClass();
});