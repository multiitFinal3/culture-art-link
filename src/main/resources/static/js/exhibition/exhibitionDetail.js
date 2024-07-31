$(document).ready(function() {
    const exhibitionId = getExhibitionIdFromUrl();
    loadExhibitionDetails(exhibitionId);

    const buttons = document.querySelectorAll('.tab-menu button');
    const contentSections = document.querySelectorAll('.content-section');


    //버튼 이벤트 관리 함수
    btnEvent()

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

function btnEvent() {


    document.addEventListener('DOMContentLoaded', function() {
        const favoriteBtn = document.getElementById('favoriteBtn');
        const notInterestedBtn = document.getElementById('notInterestedBtn');
        let starContainer = document.querySelector('.star-rating');
        const stars = starContainer.querySelectorAll('.star');

        console.log("starcontainer: ", starContainer);
        console.log("stars: ", stars);

        stars.forEach(star => {
            star.addEventListener('click', () => {
                const value = star.getAttribute('data-value');

                if(!starContainer) {
                    console.log('not found start container');
                    starContainer = document.querySelector('.star-rating');
                }
                starContainer.setAttribute('data-rating', value);
                updateStars(starContainer, stars);
            });
        });

        saveCommentBtn.addEventListener('click', function () {
            const exhibitionId = getExhibitionIdFromUrl()
            console.log("exhibition-like btn : ", exhibitionId)
            saveComment(exhibitionId, 'interested');
        });

        favoriteBtn.addEventListener('click', function () {
            const exhibitionId = getExhibitionIdFromUrl()
            console.log("exhibition-like btn : ", exhibitionId)
            updateInterest(exhibitionId, 'interested');
        });

        notInterestedBtn.addEventListener('click', function () {
            const exhibitionId = getExhibitionIdFromUrl()
            console.log("exhibition-dislike btn : ", exhibitionId)
            updateInterest(exhibitionId, 'not_interested');
        });
    })
}

function updateStars(starContainer, stars) {
    const rating = parseInt(starContainer.getAttribute('data-rating'));
    stars.forEach(star => {
        const starValue = parseInt(star.getAttribute('data-value'));
        if (starValue <= rating) {
            star.classList.add('filled');
        } else {
            star.classList.remove('filled');
        }
    });
}


async function saveComment(exhibitionId) {
    try {
        const comment = document.getElementById('commentTextarea');
        const star = starContainer.getAttribute('data-rating');
        const data = {
            star,
            content:comment.value
        }

        console.log('save comment : ',data)
        const response = await axios.post(`/exhibition/${exhibitionId}/comment`, data)

        loadExhibitionReviews()
    }catch(e) {
        console.log('error : ',e)
    }
}

async function updateInterest(exhibitionId, state) {
    console.log('update interest: ',exhibitionId, state)
    try {
        const response = await axios.post('/exhibition/interested', {
            exhibitionId: Number(exhibitionId),
            state: state
        })

        renderViewBackground(state)
    }catch(e) {
        console.log('error : ',e)
        alert(`${state === 'interested'} ? "찜 설정 실패" : "관심 없음 설정 실패"`)
    }

}

function renderViewBackground(state) {
    const contentDiv = document.querySelector(`.container`)

    console.log("dom : ",contentDiv)
    if (state === 'interested') {
        contentDiv.classList.add('background-pink')
        if (contentDiv.classList.contains('background-gray')) {
            contentDiv.classList.remove('background-gray');
        }
    }else if (state === 'not_interested') {
        contentDiv.classList.add('background-gray')
        if (contentDiv.classList.contains('background-pink')) {
            contentDiv.classList.remove('background-pink');
        }
    }
}


function getExhibitionIdFromUrl() {
    const urlParts = window.location.pathname.split('/');
    return urlParts[urlParts.length - 1];
}

async function loadExhibitionDetails(exhibitionId) {
    try {
        const exhibitionId = getExhibitionIdFromUrl()
        const response = await fetch(`/exhibition/${exhibitionId}`);
        const data = await response.json();
        console.log(data);

        data.startDate =  data.startDate?.substring(0,10) || "미정";
        data.endDate =  data.endDate?.substring(0,10) || "미정";
        await loadExhibitionReviews()
        renderExhibitionDetails(data);
    } catch (error) {
        console.error('Failed to load exhibition details:', error);
    }
}

async function loadExhibitionReviews() {
    try {
        const response = await fetch(`/exhibition/${exhibitionId}/comment`);
        const data = await response.json();
        console.log(data);
        exhibition.reviews = data
        renderReviews(data)
    } catch (error) {
        console.error('Failed to load exhibition details:', error);
    }
}

function renderExhibitionDetails(exhibition) {
    $('#exhibitionTitle').text(exhibition.title);
    $('#exhibitionMuseum').text(exhibition.museum);
    $('#exhibitionDate').text(`${exhibition.startDate} ~ ${exhibition.endDate}`);
    $('#exhibitionImage').attr('src', exhibition.image);

    console.log('exhibition : ',exhibition)

    initMap(exhibition.location);
    renderInformation(exhibition)
    renderViewBackground(exhibition?.state)
    renderVideo(exhibition.videoId);

    renderReviews(exhibition.reviews);
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
    $('#url').text(exhibition.url);
    $('#url').attr('href', exhibition.url);
    // const url = exhibition.url;
    // if (url) {
    //     $('#detailFrame').attr('src', url);
    // }
    // const url = exhibition.url;
    // if (url) {
    //     $('#detailFrame').attr('data', url);
    // }
    // URL 정보를 데이터 속성으로 저장
    // $('#exhibitionInformation').data('url', exhibition.url);
    // loadDetailUrl();
    // $('#news').text(exhibition.title);
}

function loadDetailUrl() {
    const url = $('#exhibitionInformation').data('url');
    if (url) {
        $.ajax({
            url: '/exhibition/detail-url', // Spring Boot 서버 주소로 변경
            data: { url: url },
            success: function(response) {
                console.log(response);
                $('#detailContent').html(response);
            },
            error: function() {
                $('#detailContent').html('페이지를 불러오는 데 실패했습니다.');
            }
        });
    }
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
            <div class="review-content">
                <h3>${review.name}</h3>
                <p class="review-text">${review.comment}</p>
                <div class="review-rating">${'★'.repeat(review.rating)}${'☆'.repeat(5-review.star)}</div>
            </div>
        </div>
    `).join('');
    $('#reviewsContainer').html(reviewsHtml);
}

