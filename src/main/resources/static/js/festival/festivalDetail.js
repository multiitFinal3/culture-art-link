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
        console.log("comment: ", exhibitionId)
        saveComment(exhibitionId);
    });

    saveAnalyzeBtn.addEventListener('click', function () {
        const exhibitionId = getExhibitionIdFromUrl()
        console.log("analyze : ", exhibitionId)
        saveAnalyze(exhibitionId);
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
            content:comment.value
        }

        console.log('save comment : ',data)
        const response = await axios.post(`/exhibition/exhibition/${exhibitionId}/comment`, data)

        loadExhibitionReviews()
    }catch(e) {
        console.log('error : ',e)
    }
}

// 이미지 업로드 버튼 이벤트 리스너
$('#imageUploadBtn').on('click', function() {
    $('#imageInput').click();
});

// 파일 선택 시 이벤트
$('#imageInput').on('change', function(event) {
    const file = event.target.files[0];
    if (file) {
        // 파일 선택됨을 사용자에게 알림
        $('#imageUploadBtn').text('이미지 선택됨');
    }
});

async function saveAnalyze(exhibitionId) {
    try {
        const artwork = document.getElementById('artworkTextarea');
        const analyze = document.getElementById('analyzeTextarea');
        const imageFile = document.getElementById('imageInput').files[0];

        let imagePath = null;
        if (imageFile) {
            const timestamp = new Date().getTime();
            imagePath = `/exhibition/${exhibitionId}/analyze/${timestamp}.png`;
            await uploadImageToBucket(imageFile, imagePath);
        }


        const data = {
            artwork:artwork.value,
            content:analyze.value,
            image: imagePath
        }

        console.log('save analyze : ',data)
        const response = await axios.post(`/exhibition/exhibition/${exhibitionId}/analyze`, data)

        loadExhibitionAnalyze()
    }catch(e) {
        console.log('error : ',e)
    }
}

async function uploadImageToBucket(file, path) {
    // 여기에 네이버 Bucket Management API를 사용하여 이미지를 업로드하는 코드를 구현해야 합니다.
    // 이는 서버 측에서 처리하는 것이 보안상 좋습니다.
    const formData = new FormData();
    formData.append('file', file);
    formData.append('path', path);

    await axios.post('/api/upload-to-bucket', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
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
//      const exhibitionId = getExhibitionIdFromUrl()
        const response = await fetch(`/exhibition/${exhibitionId}`);
        const data = await response.json();
        console.log(data);

        data.startDate = data.startDate?.substring(0,10) || "미정";
        data.endDate = data.endDate?.substring(0,10) || "미정";
        await loadExhibitionReviews();
        await loadExhibitionAnalyze();

        renderExhibitionDetails(data);
    } catch (error) {
        console.error('Failed to load exhibition details:', error);
    }
}

async function loadExhibitionReviews() {
    try {
        const exhibitionId = getExhibitionIdFromUrl()
        const response = await fetch(`/exhibition/exhibition/${exhibitionId}/comment`);
        const data = await response.json();
        console.log("loadExhibitionReviews : ",data);
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

    initMap(exhibition.museum);
    renderInformation(exhibition)
    renderViewBackground(exhibition?.state)
    // searchYoutubeVideos(exhibition.title, exhibition.museum);
    searchYoutubeVideos(exhibition.title);
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
            params: { query }
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
    const map = new naver.maps.Map('map', mapOptions);

    if (!location) {
        console.log('No location provided');
        $('#mapDiv').html("위치 정보가 없습니다.");
        return;
    }
    
    // 위치 정보 가져오기 
    const response = await axios.get(`/map/naver?query=${location}`);
    const locationInfo = response?.data?.items[0]

    if(!locationInfo) {
        console.error('No results found for location:', location);
        $('#mapDiv').html("해당 위치를 찾을 수 없습니다.");
        return;
    }

    // 좌표 설정
    const point = new naver.maps.LatLng(locationInfo.mapy / 10000000, locationInfo.mapx / 10000000);

    // 좌표로 이동
    map.setCenter(point);
    // 해당 좌표에 마크 설정
    new naver.maps.Marker({
        position: point,
        map: map
    });
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
    const reviewsHtml = reviews?.map(review => `
        <div class="review" data-id="${review.id}">
            <div class="review-content">
                <h3>${review.name}</h3>
                <p class="review-text">${review.content}</p>
                <div class="review-rating">${'★'.repeat(review.stars)}${'☆'.repeat(5-review.stars)}</div>
                ${(review.auth) ? '<button class="delete-review" style="position: absolute; top: 5px; right: 5px;">삭제</button>' : ''}
            </div>
        </div>
    `).join('');
    $('#reviewsContainer').html(reviewsHtml);

    $('.delete-review').on('click', function() {
        const reviewId = $(this).closest('.review').data('id');
        deleteReview(reviewId);
    });
}

async function deleteReview(reviewId) {
    try {
        const exhibitionId = getExhibitionIdFromUrl();
        const response = await axios.delete(`/exhibition/exhibition/${exhibitionId}/comment`,
            {
                data:{
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

async function loadExhibitionAnalyze() {
    try {
        const exhibitionId = getExhibitionIdFromUrl()
        const response = await fetch(`/exhibition/exhibition/${exhibitionId}/analyze`,);
        const data = await response.json();
        console.log("loadExhibitionAnalyze : ",data);
        renderAnalyze(data)
    } catch (error) {
        console.error('Failed to load exhibition details:', error);
    }
}

function renderAnalyze(analyze) {
    const analyzeHtml = analyze?.map(analyze => `
        <div class="analyze" data-id="${analyze.id}">
            <div class="analyze-content">
                <h3>${analyze.artwork}</h3>
                <p class="analyze-text">${analyze.content}</p>
                ${analyze.image ? `<img src="${analyze.image}" alt="분석 이미지" style="max-width: 100%;">` : ''}
                ${(analyze.auth) ? '<button class="update-analyze">수정</button>' : ''}
                ${(analyze.auth) ? '<button class="delete-analyze">삭제</button>' : ''}
            </div>
            <div class="analyze-edit" style="display:none;">
                <input class="edit-artwork" value="${analyze.artwork}">
                <textarea class="edit-content">${analyze.content}</textarea>
                <input type="file" class="edit-image" accept="image/*">
                <button class="save-edit">수정 완료</button>
                <button class="cancel-edit">취소</button>
            </div>
        </div>
    `).join('');
    $('#artworkAnalyzeContainer').html(analyzeHtml);

    $('.update-analyze').on('click', function() {
        const analyzeDiv = $(this).closest('.analyze');
        analyzeDiv.find('.analyze-content').hide();
        analyzeDiv.find('.analyze-edit').show();
    });

    $('.cancel-edit').on('click', function() {
        const analyzeDiv = $(this).closest('.analyze');
        analyzeDiv.find('.analyze-edit').hide();
        analyzeDiv.find('.analyze-content').show();
    });

    $('.save-edit').on('click', function() {
        const analyzeDiv = $(this).closest('.analyze');
        const analyzeId = analyzeDiv.data('id');
        const newArtwork = analyzeDiv.find('.edit-artwork').val();
        const newContent = analyzeDiv.find('.edit-content').val();
        updateAnalyze(analyzeId, newArtwork, newContent);
    });

    $('.delete-analyze').on('click', function() {
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
                data:{
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
