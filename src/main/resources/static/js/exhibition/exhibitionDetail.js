const reviewsPerPage = 5;
const analysesPerPage = 3;
let currentReviewPage = 1;
let currentAnalyzePage = 1;

const $window = $(window);


let naverMap = null

function getMapSize() {

    const $content = $("#content");
    const size = new naver.maps.Size($content.width() - 15, 444);

    return size;
}

$window.on('resize', function () {
    naverMap.setSize(getMapSize());
});

$(document).ready(function () {
    const exhibitionId = getExhibitionIdFromUrl();
    loadExhibitionDetails(exhibitionId);

    const buttons = document.querySelectorAll('.tab-menu button');
    const contentSections = document.querySelectorAll('.content-section');


    //버튼 이벤트 관리 함수
    btnEvent()

    buttons.forEach(button => {
        button.addEventListener('click', function () {
            buttons.forEach(btn => btn.classList.remove('active'));
            this.classList.add('active');

            contentSections.forEach(section => section.classList.remove('active'));

            let targetId;
            switch (this.id) {
                case 'currentBtn':
                    targetId = 'exhibitionDescription';
                    break;
                case 'pastBtn':
                    targetId = 'exhibitionInformation';
                    break;
                case 'futureBtn':
                    targetId = 'exhibitionNews';
                    break;
                case 'commentBtn':
                    targetId = 'exhibitionComment';
                    break;
                case 'AnalyzeBtn':
                    targetId = 'exhibitionAnalyze';
                    break;
            }
            document.getElementById(targetId).classList.add('active');
        });
    });
});

function renderReviewPagination(totalItems, currentPage) {
    renderPagination(totalItems, currentPage, 'reviewPagination', loadReviewPage, reviewsPerPage);
}

function renderAnalyzePagination(totalItems, currentPage) {
    renderPagination(totalItems, currentPage, 'analyzePagination', loadAnalyzePage, analysesPerPage);
}

function btnEvent() {
    const favoriteBtn = document.getElementById('favoriteBtn');
    const notInterestedBtn = document.getElementById('notInterestedBtn');

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

    let starContainer = document.querySelector('.star-rating');
    const stars = starContainer.querySelectorAll('.star');

    console.log("starcontainer: ", starContainer);
    console.log("stars: ", stars);

    stars.forEach(star => {
        star.addEventListener('click', () => {
            const value = star.getAttribute('data-value');
            if (!starContainer) {
                console.log('not found start container');
                starContainer = document.querySelector('.star-rating');
            }
            starContainer.setAttribute('data-rating', value);
            updateStars(starContainer, stars);
        });
    });

    saveCommentBtn.addEventListener('click', function () {
        const exhibitionId = getExhibitionIdFromUrl()
        console.log("comment: ", exhibitionId)
        saveComment(exhibitionId);
    });

    saveAnalyzeBtn.addEventListener('click', function () {
        const exhibitionId = getExhibitionIdFromUrl()
        console.log("analyze : ", exhibitionId)
        saveAnalyze(exhibitionId);
    });

    $('#imageUploadBtn').on('click', function () {
        $('#imageInput').click();
    });

// 파일 선택 시 이벤트
    $('#imageInput').on('change', function (event) {
        const file = event.target.files[0];
        if (file) {
            // 파일 선택됨을 사용자에게 알림
            $('#imageUploadBtn').text('이미지 선택됨');

            // 이미지 미리보기
            const reader = new FileReader();
            reader.onload = function (e) {
                $('#previewImage').attr('src', e.target.result);
                $('#imagePreview').show();
            }
            reader.readAsDataURL(file);
        }
    });

// 이미지 제거 버튼 이벤트 리스너
    $('#removeImageBtn').on('click', function () {
        $('#imageInput').val('');
        $('#imagePreview').hide();
        $('#imageUploadBtn').text('이미지 첨부');
    });

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
        const starContainer = document.querySelector('.star-rating');
        const stars = starContainer.getAttribute('data-rating');
        const data = {
            stars,
            content: comment.value
        }

        console.log('save comment : ', data)
        const response = await axios.post(`/exhibition/exhibition/${exhibitionId}/comment`, data)


        loadExhibitionReviews()


        $("#commentTextarea").val('')
        starContainer.setAttribute('data-rating', 0);


        const $stars = starContainer.querySelectorAll('.star');
        updateStars(starContainer, $stars);

        alert('작성 완료');

    } catch (e) {
        console.log('error : ', e)
    }
}


async function saveAnalyze(exhibitionId) {
    try {
        const artwork = document.getElementById('artworkTextarea');
        const analyze = document.getElementById('analyzeTextarea');
        const imageFile = document.getElementById('imageInput').files[0];

        let imagePath = null;

        console.log('imageFile, imagePath : ', imageFile, imagePath)
        if (imageFile) {
            const timestamp = new Date().getTime();
            imagePath = `/exhibition/${exhibitionId}/analyze/${timestamp}.png`;
            await uploadImageToBucket(imageFile, imagePath);
        }


        const data = {
            artwork: artwork.value,
            content: analyze.value,
            image: imagePath
        }

        console.log('save analyze : ', data)
        const response = await axios.post(`/exhibition/exhibition/${exhibitionId}/analyze`, data)

        loadExhibitionAnalyze()

        alert('작성 완료');
        $("#artworkTextarea").val('')
        $("#analyzeTextarea").val('')
        $("#imageInput").val('')
        $("#previewImage").val('')
    } catch (e) {
        console.log('error : ', e)
    }
}

async function uploadImageToBucket(file, path) {
    // 데이터 규칙
    const formData = new FormData();
    formData.append('file', file);
    formData.append('path', path);

    await axios.post('/file/upload', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}

// function setInterestedBtn(state) {
//     let $downButton = $(`#notInterestedBtn img`);
//     let $upButton = $(`#favoriteBtn img`);
//     const baseUrl = 'https://kr.object.ncloudstorage.com/team3/common/';
//
//     $downButton.attr('src', baseUrl + 'downNo.png');
//     $upButton.attr('src', baseUrl + 'upNo.png');
//
//     let downSrc = $downButton.attr('src');
//     let upSrc = $upButton.attr('src');
//     console.log('setInterestedBtn : ', state)
//
//     if (state === 'interested') {
//         if (upSrc.includes('upBlue.png')) {
//             $upButton.attr('src', upSrc.replace('upBlue.png', 'upNo.png'));
//         } else if (upSrc.includes('upNo.png')) {
//             console.log('no')
//             $upButton.attr('src', upSrc.replace('upNo.png', 'upBlue.png'));
//         }
//         $downButton.attr('src', baseUrl + 'downNo.png');
//     }else if(state ==='not_interested') {
//         if (downSrc.includes('downRed.png')) {
//             $downButton.attr('src', downSrc.replace('downRed.png', 'downNo.png'));
//         } else if (downSrc.includes('downNo.png')) {
//             $downButton.attr('src', downSrc.replace('downNo.png', 'downRed.png'));
//         }
//         $upButton.attr('src', baseUrl + 'upNo.png');
//     }
// }

function setInterestedBtn(state) {
    let $downButton = $(`#notInterestedBtn img`);
    let $upButton = $(`#favoriteBtn img`);
    const baseUrl = 'https://kr.object.ncloudstorage.com/team3/common/';
    if (!$downButton.attr('src') && !$upButton.attr('src')) {
        $downButton.attr('src', baseUrl + 'downNo.png');
        $upButton.attr('src', baseUrl + 'upNo.png');
    }
    if (state === 'interested') {
        if ($upButton.attr('src').includes('upBlue.png')) {
            $upButton.attr('src', baseUrl + 'upNo.png');
        } else {
            $upButton.attr('src', baseUrl + 'upBlue.png');
        }
        $downButton.attr('src', baseUrl + 'downNo.png');
    } else if (state === 'not_interested') {
        if ($downButton.attr('src').includes('downRed.png')) {
            $downButton.attr('src', baseUrl + 'downNo.png');
        } else {
            $downButton.attr('src', baseUrl + 'downRed.png');
        }
        $upButton.attr('src', baseUrl + 'upNo.png');
    }
}


async function updateInterest(exhibitionId, state) {
    console.log('update interest: ', exhibitionId, state);
    try {
        const response = await axios.post('/exhibition/interested', {
            exhibitionId: Number(exhibitionId),
            state: state
        });
        setInterestedBtn(state);
    } catch (e) {
        console.log('error : ', e);
        alert(state === 'interested' ? "찜 설정 실패" : "관심 없음 설정 실패");
    }
}


function getExhibitionIdFromUrl() {
    const urlParts = window.location.pathname.split('/');
    return urlParts[urlParts.length - 1];
}

async function loadExhibitionDetails(exhibitionId) {
    try {
//      const exhibitionId = getExhibitionIdFromUrl()
        const response = await fetch(`/exhibition/${exhibitionId}`);
        const data = await response.json();
        console.log(data);

        data.startDate = data.startDate?.substring(0, 10) || "미정";
        data.endDate = data.endDate?.substring(0, 10) || "미정";
        await loadExhibitionReviews();
        await loadExhibitionAnalyze();

        await loadExhibitionKeyword()

        // const ratingResponse = await fetch(`/exhibition/${exhibitionId}/rating`);
        // const ratingData = await ratingResponse.json();
        $('#averageRating').text(Number(data?.starsAVG).toFixed(1));

        renderExhibitionDetails(data);
    } catch (error) {
        console.error('Failed to load exhibition details:', error);
    }
}

function renderPagination(totalItems, currentPage, containerId, loadPageFunction, itemsPerPage) {
    const totalPages = Math.ceil(totalItems / itemsPerPage);
    let paginationHtml = '';

    const maxButtons = 5;
    let startPage = Math.max(1, currentPage - Math.floor(maxButtons / 2));
    let endPage = Math.min(totalPages, startPage + maxButtons - 1);

    if (endPage - startPage + 1 < maxButtons) {
        startPage = Math.max(1, endPage - maxButtons + 1);
    }

    for (let i = startPage; i <= endPage; i++) {
        paginationHtml += `<button class="page-btn ${containerId}-btn ${i === currentPage ? 'active' : ''}" data-page="${i}">${i}</button>`;
    }

    $(`#${containerId}`).html(paginationHtml);

    $(`.${containerId}-btn`).on('click', function () {
        const page = $(this).data('page');
        loadPageFunction(page);
    });
}

function loadReviewPage(page) {
    currentReviewPage = page;
    loadExhibitionReviews();
}

function loadAnalyzePage(page) {
    currentAnalyzePage = page;
    loadExhibitionAnalyze();
}

async function loadExhibitionReviews() {
    try {
        const exhibitionId = getExhibitionIdFromUrl()
        const response = await fetch(`/exhibition/exhibition/${exhibitionId}/comment`);
        const data = await response.json();
        console.log("loadExhibitionReviews : ", data);
        renderReviews(data)
    } catch (error) {
        console.error('Failed to load exhibition details:', error);
    }
}

function renderExhibitionDetails(exhibition) {
    $('#exhibitionTitle').text(exhibition.title);
    $('#exhibitionArtist').text(exhibition.artist);
    $('#exhibitionMuseum').text(exhibition.museum);
    // 날짜 형식 변경
    const formatDate = (dateString) => {
        if (dateString === "미정") return "미정";
        const date = new Date(dateString);
        return `${date.getFullYear()}.${String(date.getMonth() + 1).padStart(2, '0')}.${String(date.getDate()).padStart(2, '0')}`;
    };

    const startDate = formatDate(exhibition.startDate);
    const endDate = formatDate(exhibition.endDate);
    $('#exhibitionDate').text(`${startDate} - ${endDate}`);
    $('#exhibitionImage').attr('src', exhibition.image);


    initMap(exhibition.museum);
    renderInformation(exhibition)
    // searchYoutubeVideos(exhibition.title, exhibition.museum);
    searchYoutubeVideos(exhibition.title);
    setInterestedBtn(exhibition.state)
}

// async function searchYoutubeVideos(title, museum) {
//     try {
//         const response = await axios.post('/exhibition/videos', null, {
//             params: { title, museum }
//         });
//         const videos = response.data;
//         renderVideos(videos);
//     } catch (error) {
//         console.error('Failed to search YouTube videos:', error);
//     }
// }

async function searchYoutubeVideos(query) {
    try {
        const response = await axios.post('/exhibition/videos', null, {
            params: {query}
        });
        const videos = response.data;
        renderVideos(videos);
    } catch (error) {
        console.error('Failed to search YouTube videos:', error);
    }
}

function renderVideos(videos) {
    const videosHtml = videos.map(video => `
    <div class="video-item">
      <a href="${video.link}" target="_blank">
        <img src="${video.thumbnailUrl}" alt="${video.title}">
        <p>${video.title}</p>
      </a>
    </div>
  `).join('');
    $('#videoContainer').html(videosHtml);
}


async function initMap(location) {
    console.log('Location:', location); // 위치 정보 로깅

    const mapOptions = {
        center: new naver.maps.LatLng(37.3595704, 127.105399),
        zoom: 15
    };

    const mapDiv = document.getElementById('map');
    naverMap = new naver.maps.Map(mapDiv, mapOptions);


    console.log('mapDiv : ', mapDiv)
    console.log('map : ', naverMap)
    if (!location) {
        console.log('No location provided');
        $('#mapDiv').html("위치 정보가 없습니다.");
        return;
    }

    // 위치 정보 가져오기 
    const response = await axios.get(`/map/naver?query=${location}`);
    const locationInfo = response?.data?.items[0]

    if (!locationInfo) {
        console.error('No results found for location:', location);
        $('#mapDiv').html("해당 위치를 찾을 수 없습니다.");
        return;
    }

    // 좌표 설정
    const point = new naver.maps.LatLng(locationInfo.mapy / 10000000, locationInfo.mapx / 10000000);

    // 좌표로 이동
    naverMap.setCenter(point);
    // 해당 좌표에 마크 설정
    await new naver.maps.Marker({
        position: point,
        map: naverMap
    });

    setTimeout(function () {
        window.dispatchEvent(new Event('resize'));
    }, 600);
}

function renderInformation(exhibition) {
    $('#description').html(exhibition.description);
    $('#subDescription').text(exhibition.subDescription);
    $('#url').text(exhibition.url);
    $('#url').attr('href', exhibition.url);
}

function renderVideo(videoId) {
    $('#videoContainer').html(`
        <iframe width="560" height="315" 
                src="https://www.youtube.com/embed/${videoId}" 
                frameborder="0" allowfullscreen></iframe>
    `);
}

function renderReviews(reviews) {
    const startIndex = (currentReviewPage - 1) * reviewsPerPage;
    const endIndex = startIndex + reviewsPerPage;
    const pageReviews = reviews.slice(startIndex, endIndex);

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        const day = date.getDate();
        const hours = date.getHours();
        const minutes = date.getMinutes();
        const seconds = date.getSeconds();
        const ampm = hours >= 12 ? '오후' : '오전';
        const formattedHours = hours % 12 || 12;

        return `${year}. ${month}. ${day}. ${ampm} ${formattedHours}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
    };

    const reviewsHtml = pageReviews.map(review => `
        <div class="review" data-id="${review.id}">
            <div class="review-top">
                <img src="https://kr.object.ncloudstorage.com/team3${review.profileImage}" alt="프로필 이미지" class="profile-image">
                <div class="review-info">
                    <h3 class="review-name">${review.name}</h3>
                    <p class="review-date">${formatDate(review.createdAt)}</p>
                    <div class="review-rating">${'★'.repeat(review.stars)}${'☆'.repeat(5 - review.stars)}</div>
                </div>
                ${(review.auth) ? '<button class="delete-review btn btn-outline-secondary">삭제</button>' : ''}
            </div>
            <div class="review-bottom">
                <p class="review-text">${review.content}</p>
            </div>
        </div>
    `).join('');

    $('#reviewsContainer').html(reviewsHtml);
    renderReviewPagination(reviews.length, currentReviewPage);

    $('.delete-review').on('click', function () {
        const reviewId = $(this).closest('.review').data('id');
        deleteReview(reviewId);
    });
}

// function renderReviews(reviews) {
//     const startIndex = (currentReviewPage - 1) * reviewsPerPage;
//     const endIndex = startIndex + reviewsPerPage;
//     const pageReviews = reviews.slice(startIndex, endIndex);
//
//
//     const reviewsHtml = pageReviews.map(review => `
//         <div class="review" data-id="${review.id}">
//             <div class="review-top">
//                 <img src="https://kr.object.ncloudstorage.com/team3${review.profileImage}" alt="프로필 이미지" class="profile-image">
//                 <div class="review-info">
//                     <h3 class="review-name">${review.name}</h3>
//                     <p class="review-date">${review.createdAt}</p>
//                     <div class="review-rating">${'★'.repeat(review.stars)}${'☆'.repeat(5 - review.stars)}</div>
//                 </div>
//                 ${(review.auth) ? '<button class="delete-review btn btn-outline-secondary">삭제</button>' : ''}
//             </div>
//             <div class="review-bottom">
//                 <p class="review-text">${review.content}</p>
//             </div>
//         </div>
//     `).join('');
//
//     $('#reviewsContainer').html(reviewsHtml);
//     renderReviewPagination(reviews.length, currentReviewPage);
//
//
//     $('.delete-review').on('click', function () {
//         const reviewId = $(this).closest('.review').data('id');
//         deleteReview(reviewId);
//     });
// }

async function deleteReview(reviewId) {
    try {
        const exhibitionId = getExhibitionIdFromUrl();
        const response = await axios.delete(`/exhibition/exhibition/${exhibitionId}/comment`,
            {
                data: {
                    id: reviewId
                }
            });

        if (response.status === 200) {
            loadExhibitionReviews();
        } else {
            console.error('Failed to delete review');
        }
    } catch (error) {
        console.error('Error deleting review:', error);
    }
}

async function loadExhibitionKeyword() {
    try {
        const exhibitionId = getExhibitionIdFromUrl()
        const response = await fetch(`/exhibition/exhibition/${exhibitionId}/keyword`);
        const data = await response.json();
        console.log("loadExhibitionKeyword : ", data);
        renderkeyword(data)
    } catch (error) {
        console.error('Failed to load exhibition details:', error);
    }
}


function renderkeyword(keyword) {

    for (const key of Object.keys(keyword)) {
        const keywordHtml = keyword[key].slice(0, 5).map(data => `
        <a href="#" class="keyword">${data.keyword}</a>
    `).join('');

        const id = key === "keyword" ? "#exhibitionKeyword" : "#exhibitionCommentKeyword";
        $(id).html(keywordHtml);
    }
}


async function loadExhibitionAnalyze() {
    try {
        const exhibitionId = getExhibitionIdFromUrl()
        const response = await fetch(`/exhibition/exhibition/${exhibitionId}/analyze`);
        const data = await response.json();
        console.log("loadExhibitionAnalyze : ", data);
        renderAnalyze(data)
    } catch (error) {
        console.error('Failed to load exhibition details:', error);
    }
}

function renderAnalyze(analyze) {
    const startIndex = (currentAnalyzePage - 1) * analysesPerPage;
    const endIndex = startIndex + analysesPerPage;
    const pageAnalyze = analyze.slice(startIndex, endIndex);


    const analyzeHtml = pageAnalyze.map(analyze => `
         <div class="analyze" data-id="${analyze.id}">
            <div class="analyze-content">
                <div class="analyze-text-content">
                    <h3 class="analyze-username">${analyze.name}</h3>
                    <h4 class="analyze-artwork">${analyze.artwork}</h4>
                    <br>
                    <p class="analyze-text">${analyze.content}</p>
                </div>
                ${analyze.image ? `
                    <div class="analyze-image">
                        <img src="https://kr.object.ncloudstorage.com/team3${analyze.image}" alt="분석 이미지">
                    </div>
                ` : ''}
            </div>
            <div class="analyze-buttons">
                ${(analyze.auth) ? '<button class="update-analyze btn btn-outline-secondary">수정</button>' : ''}
                ${(analyze.auth) ? '<button class="delete-analyze btn btn-outline-secondary">삭제</button>' : ''}
            </div>
            <div class="analyze-edit" style="display:none;">
                <h3>${analyze.artwork}</h3>
                <textarea class="edit-content">${analyze.content}</textarea>
                <button class="save-edit">수정 완료</button>
                <button class="cancel-edit">취소</button>
            </div>
        </div>
`).join('');
    $('#artworkAnalyzeContainer').html(analyzeHtml);
    renderAnalyzePagination(analyze.length, currentAnalyzePage);


    $('.update-analyze').on('click', function () {
        const analyzeDiv = $(this).closest('.analyze');
        analyzeDiv.find('.analyze-content').hide();
        analyzeDiv.find('.analyze-edit').show();
    });

    $('.cancel-edit').on('click', function () {
        const analyzeDiv = $(this).closest('.analyze');
        analyzeDiv.find('.analyze-edit').hide();
        analyzeDiv.find('.analyze-content').show();
    });

    $('.save-edit').on('click', function () {
        const analyzeDiv = $(this).closest('.analyze');
        const analyzeId = analyzeDiv.data('id');
        const newArtwork = analyzeDiv.find('.edit-artwork').val();
        const newContent = analyzeDiv.find('.edit-content').val();
        updateAnalyze(analyzeId, newArtwork, newContent);
    });

    $('.delete-analyze').on('click', function () {
        const analyzeId = $(this).closest('.analyze').data('id');
        deleteAnalyze(analyzeId);
    });
}

async function updateAnalyze(analyzeId, artwork, content, image) {
    try {
        const exhibitionId = getExhibitionIdFromUrl();
        const response = await axios.patch(`/exhibition/exhibition/${exhibitionId}/analyze`, {
            id: analyzeId,
            artwork: artwork,
            content: content,
            image: image
        });

        if (response.status === 200) {
            loadExhibitionAnalyze();
        } else {
            console.error('Failed to update analyze');
        }
    } catch (error) {
        console.error('Error updating analyze:', error);
    }
}

async function deleteAnalyze(analyzeId) {
    try {
        const exhibitionId = getExhibitionIdFromUrl();
        const response = await axios.delete(`/exhibition/exhibition/${exhibitionId}/analyze`,
            {
                data: {
                    id: analyzeId
                }
            });

        if (response.status === 200) {
            loadExhibitionAnalyze();
        } else {
            console.error('Failed to delete review');
        }
    } catch (error) {
        console.error('Error deleting review:', error);
    }
}
