$(document).ready(function() {
    const propertiesContainer = $('.nearby-properties-container');
    const items = propertiesContainer.children('.nearby-property');
    const totalItems = items.length;
    const itemsPerPage = 5; // 한 페이지에 보여줄 이미지 개수
    let currentIndex = 0;

    function showPage(index) {
        items.hide(); // 모든 이미지를 숨김
        items.slice(index, index + itemsPerPage).show(); // 현재 페이지의 이미지 보여줌
    }

    $('.prev').click(function() {
        if (currentIndex > 0) {
            currentIndex--; // 인덱스 감소
            showPage(currentIndex);
        }
    });

    $('.next').click(function() {
        if (currentIndex + itemsPerPage < totalItems) { // 최대 인덱스 체크
            currentIndex++; // 인덱스 증가
            showPage(currentIndex);
        }
    });

    // 초기 페이지 로드
    showPage(0);

//    // 좋아요, 싫어요 버튼 이벤트 리스너
//    const likeBtn = document.querySelector('.like-btn');
//    const dislikeBtn = document.querySelector('.dislike-btn');
//
//    likeBtn.addEventListener('click', function() {
//        updateInterest('like');
//    });
//
//    dislikeBtn.addEventListener('click', function() {
//        updateInterest('dislike');
//    });
//
//    function updateInterest(action) {
//        const culturalPropertiesId = window.location.pathname.split('/').slice(-2, -1)[0]; // URL에서 ID 추출
//
//        fetch(`/cultural-properties/detail/${culturalPropertiesId}`, {
//            method: 'POST',
//            headers: {
//                'Content-Type': 'application/x-www-form-urlencoded'
//            },
//            body: new URLSearchParams({
//                'action': action
//            })
//        })
//        .then(response => {
//            if (response.ok) {
//                return response.json(); // JSON 응답 처리
//            }
//            throw new Error('Network response was not ok.');
//        })
//        .then(data => {
//            // 버튼 상태 업데이트
//            if (action === 'like') {
//                likeBtn.classList.add('liked');
//                dislikeBtn.classList.remove('disliked'); // 필요 시 상태 초기화
//            } else {
//                dislikeBtn.classList.add('disliked');
//                likeBtn.classList.remove('liked'); // 필요 시 상태 초기화
//            }
//        })
//        .catch(error => {
//            console.error('Error:', error);
//        });
//    }

//    const urlParts = window.location.pathname.split('/');
//    const culturalPropertiesId = urlParts[urlParts.length - 2];



    function loadRecentReviews() {
        const urlParts = window.location.pathname.split('/');
        const culturalPropertiesId = urlParts[urlParts.length - 1];

        console.log('Cultural Properties ID:', culturalPropertiesId);

        fetch(`/cultural-properties/detail/${culturalPropertiesId}/review`) // 리뷰 조회 API 호출
            .then(response => {
                if (!response.ok) {
                    throw new Error('네트워크 오류 발생');
                }
                return response.json(); // JSON 형식으로 응답받기
            })
            .then(data => {
                console.log('API Response:', data); // 응답 확인용 로그

                // 데이터 검증
                if (!data || !data.reviews || !Array.isArray(data.reviews)) {
                    throw new Error('잘못된 응답 형식');
                }

                const reviews = data.reviews; // 리뷰 리스트
                const totalReviews = data.totalReviews || 0; // 총 리뷰 수, 기본값으로 0
                const reviewsContainer = document.getElementById('reviewsList');

                reviewsContainer.innerHTML = ''; // 기존 리뷰 초기화

                // 총 리뷰 개수 업데이트
                document.querySelector('#totalReviewCount span').textContent = totalReviews;

                // 리뷰가 존재하지 않으면 메시지 추가
                if (totalReviews === 0) {
                    reviewsContainer.innerHTML = '<li class="no-reviews">리뷰가 아직 없습니다. </li>';
                } else {
                    // 각 리뷰를 HTML로 생성하여 추가
                    reviews.forEach(review => {
                        const reviewHtml = `
                            <li class="review">
                                <div class="user-info">
                                    <img src="${review.userProfileImage || '/img/festival/noPhoto.png'}" alt="${review.userName}">
                                    <div class="user-details">
                                        <span>${review.userName}</span>
                                        <span style="font-size: 13px; color: #999;">${new Date(review.createdAt).toLocaleString()}</span>
                                        <div class="review-rating" data-rating="${review.star}">
                                            ${Array.from({ length: 5 }, (_, index) => {
                                                return index < review.star ?
                                                    `<span class="review-star">&#9733;</span>` :
                                                    `<span class="review-star empty">&#9734;</span>`;
                                            }).join('')}
                                        </div>
                                    </div>
                                </div>
                                <div class="review-content">
                                    <p>${review.content}</p>
                                </div>
                            </li>
                        `;
                        reviewsContainer.innerHTML += reviewHtml; // 리뷰 추가
                    });
                }
            })
            .catch(error => {
                console.error('리뷰 로드 실패:', error);
            });
    }



//    console.log('JavaScript file is loaded');
    loadRecentReviews();


});




//$(document).ready(function() {
//    const propertiesContainer = $('.nearby-properties-container');
//    const items = propertiesContainer.children('.nearby-property');
//    const totalItems = items.length;
//    const itemsPerPage = 5; // 한 페이지에 보여줄 이미지 개수
//    let currentIndex = 0;
//
//    function showPage(index) {
//        items.hide(); // 모든 이미지를 숨김
//        items.slice(index, index + itemsPerPage).show(); // 현재 페이지의 이미지 보여줌
//    }
//
//    $('.prev').click(function() {
//        if (currentIndex > 0) {
//            currentIndex--; // 인덱스 감소
//            showPage(currentIndex);
//        }
//    });
//
//    $('.next').click(function() {
//        if (currentIndex + itemsPerPage < totalItems) { // 최대 인덱스 체크
//            currentIndex++; // 인덱스 증가
//            showPage(currentIndex);
//        }
//    });
//
//    // 초기 페이지 로드
//    showPage(0);
//
//
//
//    document.addEventListener('DOMContentLoaded', function() {
//        const likeBtn = document.querySelector('.like-btn');
//        const dislikeBtn = document.querySelector('.dislike-btn');
//
//        likeBtn.addEventListener('click', function() {
//            updateInterest('like');
//        });
//
//        dislikeBtn.addEventListener('click', function() {
//            updateInterest('dislike');
//        });
//
//        function updateInterest(action) {
//            const culturalPropertiesId = window.location.pathname.split('/').slice(-2, -1)[0]; // URL에서 ID 추출
//
//            fetch(`/cultural-properties/detail/${culturalPropertiesId}/interest`, {
//                method: 'POST',
//                headers: {
//                    'Content-Type': 'application/x-www-form-urlencoded'
//                },
//                body: new URLSearchParams({
//                    'action': action
//                })
//            })
//            .then(response => {
//                if (response.ok) {
//                    return response.json(); // JSON 응답 처리
//                }
//                throw new Error('Network response was not ok.');
//            })
//            .then(data => {
//                // 버튼 상태 업데이트
//                if (action === 'like') {
//                    likeBtn.classList.add('liked');
//                    dislikeBtn.classList.remove('disliked'); // 필요 시 상태 초기화
//                } else {
//                    dislikeBtn.classList.add('disliked');
//                    likeBtn.classList.remove('liked'); // 필요 시 상태 초기화
//                }
//            })
//            .catch(error => {
//                console.error('Error:', error);
//            });
//        }
//    });
//
//
//});



