setTimeout(() => {
    const banner = document.getElementById('notification-banner');
    if (banner) banner.style.display = 'none';
}, 15000);

function hideBanner() {
    const banner = document.getElementById('notification-banner');
    if (banner) banner.style.display = 'none';
}

function setPollutionClass() {
    const pollutionCard = document.getElementById('pollution-card');
    if (pollutionCard) {
        const level = parseInt(pollutionCard.getAttribute('data-pollution'));
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
        const index = parseInt(uvCard.getAttribute('data-uv'));
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
        const future = parseInt(pollutionCard.getAttribute('data-future'));
        const current = parseInt(pollutionCard.getAttribute('data-current'));
        pollutionCard.classList.remove('pollution-good', 'pollution-moderate', 'pollution-unhealthy');

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