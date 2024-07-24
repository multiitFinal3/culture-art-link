$(document).ready(function() {
    const exhibitionId = getExhibitionIdFromUrl();
    loadExhibitionDetails(exhibitionId);

    const buttons = document.querySelectorAll('.tab-menu button');
    const contentSections = document.querySelectorAll('.content-section');

    buttons.forEach(button => {
        button.addEventListener('click', function() {
            buttons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');

            contentSections.forEach(section => section.classList.remove('active'));

            let targetId;
            switch(this.id) {
                case 'currentBtn':
                    targetId = 'exhibitionDescription';
                    break;
                case 'pastBtn':
                    targetId = 'exhibitionInformation';
                    break;
                case 'futureBtn':
                    targetId = 'exhibitionNews';
                    break;
            }
            document.getElementById(targetId).classList.add('active');
        });
    });
});

function getExhibitionIdFromUrl() {
    const urlParts = window.location.pathname.split('/');
    return urlParts[urlParts.length - 1];
}

async function loadExhibitionDetails(exhibitionId) {
    try {
        const response = await fetch(`/exhibition/${exhibitionId}`);
        const data = await response.json();
        console.log(data);
        renderExhibitionDetails(data);
    } catch (error) {
        console.error('Failed to load exhibition details:', error);
    }
}

function renderExhibitionDetails(exhibition) {
    $('#exhibitionTitle').text(exhibition.title);
    $('#exhibitionMuseum').text(exhibition.museum);
    $('#exhibitionDate').text(`${exhibition.startDate} ~ ${exhibition.endDate}`);
    $('#exhibitionImage').attr('src', exhibition.image);

    initMap(exhibition.location);
    renderInformation(exhibition)
    renderVideo(exhibition.videoId);
    // renderReviews(exhibition.reviews);
    renderRelatedExhibitions(exhibition.relatedExhibitions);
}


function initMap(location) {
    const map = new google.maps.Map(document.getElementById("map"), {
        zoom: 15,
        center: location,
    });
    new google.maps.Marker({
        position: location,
        map: map,
    });
}

function renderInformation(exhibition) {
    $('#description').html(exhibition.description);
    $('#subDescription').text(exhibition.subDescription);
    const url = exhibition.url;
    if (url) {
        $('#detailFrame').attr('src', url);
    }
    // $('#news').text(exhibition.title);
}

function renderVideo(videoId) {
    $('#videoContainer').html(`
        <iframe width="560" height="315" 
                src="https://www.youtube.com/embed/${videoId}" 
                frameborder="0" allowfullscreen></iframe>
    `);
}

function renderReviews(reviews) {
    const reviewsHtml = reviews.map(review => `
        <div class="review">
            <img src="${review.reviewerImage}" alt="${review.reviewerName}" class="reviewer-image">
            <div class="review-content">
                <h3>${review.reviewerName}</h3>
                <p class="review-text">${review.text}</p>
                <div class="review-rating">${'★'.repeat(review.rating)}${'☆'.repeat(5-review.rating)}</div>
            </div>
        </div>
    `).join('');
    $('#reviewsContainer').html(reviewsHtml);
}

function renderRelatedExhibitions(relatedExhibitions) {
    const relatedHtml = relatedExhibitions.map(exhibition => `
        <div class="related-exhibition">
            <img src="${exhibition.image}" alt="${exhibition.title}">
            <h3>${exhibition.title}</h3>
            <p>${exhibition.museum}</p>
        </div>
    `).join('');
    $('#relatedExhibitionsContainer').html(relatedHtml);
}