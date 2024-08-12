$(document).ready(function() {
    const propertiesContainer = $('.nearby-properties-container');
    const items = propertiesContainer.children('.nearby-property');
    const totalItems = items.length;
    const itemsPerPage = 5;
    let currentIndex = 0;

    function showPage(index) {
        items.hide();
        items.slice(index, index + itemsPerPage).show();
    }

    $('.prev').click(function() {
        if (currentIndex > 0) {
            currentIndex--;
            showPage(currentIndex);
        }
    });

    $('.next').click(function() {
        if (currentIndex + itemsPerPage < totalItems) {
            currentIndex++;
            showPage(currentIndex);
        }
    });

    showPage(0);

    function loadRecentReviews() {
        const urlParts = window.location.pathname.split('/');
        const culturalPropertiesId = urlParts[urlParts.length - 1];

        fetch(`/cultural-properties/detail/${culturalPropertiesId}/review`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('네트워크 오류 발생');
                }
                return response.json();
            })
            .then(data => {
                const reviews = data.reviews;
                const totalReviews = data.totalReviews || 0;
                const reviewsContainer = document.getElementById('reviewsList');

                reviewsContainer.innerHTML = '';
                document.querySelector('#totalReviewCount').textContent = totalReviews;

                if (totalReviews === 0) {
                    reviewsContainer.innerHTML = '<li class="no-reviews">리뷰가 아직 없습니다.</li>';
                } else {
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
                        reviewsContainer.innerHTML += reviewHtml;
                    });
                }
            })
            .catch(error => {
                console.error('리뷰 로드 실패:', error);
            });
    }

    loadRecentReviews();

    $('#likeButton').click(function() {
        const culturalPropertiesId = window.location.pathname.split('/').pop();
        const isLiked = $(this).data('liked');
        updateInterest(culturalPropertiesId, isLiked ? 'unlike' : 'like');
    });

    $('#dislikeButton').click(function() {
        const culturalPropertiesId = window.location.pathname.split('/').pop();
        const isDisliked = $(this).data('disliked');
        updateInterest(culturalPropertiesId, isDisliked ? 'undislike' : 'dislike');
    });

    function updateInterest(culturalPropertiesId, action) {
        $.ajax({
            url: `/cultural-properties/updateInterest`,
            type: 'POST',
            data: { culturalPropertiesId: culturalPropertiesId, action: action },
            success: function(response) {
                if (response.success) {
                    updateButtonState(action);
                }
            },
            error: function(xhr, status, error) {
                console.error('관심 업데이트 실패:', error);
            }
        });
    }

    function updateButtonState(action) {
        const likeButton = $('#likeButton');
        const dislikeButton = $('#dislikeButton');

        switch(action) {
            case 'like':
                likeButton.data('liked', true).addClass('active');
                dislikeButton.data('disliked', false).removeClass('active');
                break;
            case 'unlike':
                likeButton.data('liked', false).removeClass('active');
                break;
            case 'dislike':
                dislikeButton.data('disliked', true).addClass('active');
                likeButton.data('liked', false).removeClass('active');
                break;
            case 'undislike':
                dislikeButton.data('disliked', false).removeClass('active');
                break;
        }
    }

//    $("#showReviewsBtn").click(function() {
//        const culturalPropertiesId = window.location.pathname.split('/').pop();
//        window.location.href = `/cultural-properties/detail/${culturalPropertiesId}/review/detail`;
//    });


    // 페이지 로드 시 평균 별점 가져오기
    const averageRating = parseFloat($('.averageRating').data('average-rating')); // 평균 평점 가져오기
    console.log("Average Rating from data attribute: " + averageRating); // 평균 별점 로그
    if (!isNaN(averageRating)) {
        updateAvgStarDisplay($('.averageRating'), averageRating); // 별점 업데이트
    } else {
        updateAvgStarDisplay($('.averageRating'), 0); // 기본값 0으로 설정
    }



    function updateAvgStarDisplay(container, averageRating) {
        const fullStars = Math.floor(averageRating); // 소수점 아래 버림
    //    const hasHalfStar = (averageRating % 1) >= 0.5; // 소수점이 0.5 이상이면 반별 표시
        const hasHalfStar = (averageRating % 1) >= 0.25; // 소수점이 0.25 이상이면 반별 표시

        container.find('.review-avgstar').empty(); // 이전 별점 초기화


        // 별 점수 반영
        for (let i = 0; i < 5; i++) {
            if (i < fullStars) {
                container.find('.review-avgstar').append('<span class="avgstar full">&#9733;</span>'); // 채워진 별 노란색
            } else if (hasHalfStar && i === fullStars) {
        //            container.find('.review-avgstar').append('<span class="avgstar half"></span>'); // 반별 추가
                container.find('.review-avgstar').append('<span class="avgstar half" style="color: gold;">&#9733;</span>'); // 반별 추가
        //            container.find('.review-avgstar').append('<span class="avgstar half">&#9733;</span><span class="avgstar empty">&#9734;</span>'); // 반별 추가
        //            container.find('.review-avgstar').append('<span class="avgstar half">&#9734;</span>'); // 반별 추가
        //            container.find('.review-avgstar').append('<span class="avgstar half" style="color: gold;">&#9733;</span><span class="avgstar half" style="color: lightgray;">&#9734;</span>'); // 반별로 채워진 별과 빈 별 조합
            } else {
                container.find('.review-avgstar').append('<span class="avgstar empty">&#9734;</span>'); // 빈 별 회색
            }
        }
    }


//    const detailButton = document.querySelector('.button-box');
//    const largeCard = document.querySelector('.large-card');
//    const mapButton = document.getElementById('showMapBtn');
//    const reviewButton = document.getElementById('showReviewsBtn');
//
//    detailButton.addEventListener('click', function() {
//        largeCard.style.display = 'block';
//    });
//
//    mapButton.addEventListener('click', function() {
//        largeCard.style.display = 'none';
////        showMap();
//    });
//
//    reviewButton.addEventListener('click', function() {
//        largeCard.style.display = 'none';
//    });


    const detailButton = document.querySelector('.button-box');
    const mapButton = document.getElementById('showMapBtn');
    const reviewButton = document.getElementById('showReviewsBtn');
    const detailInfo = document.getElementById('detailInfo');
    const mapInfo = document.getElementById('mapInfo');

    detailButton.addEventListener('click', function() {
        detailInfo.style.display = 'block';
        mapInfo.style.display = 'none';
        reviewInfo.style.display = 'none';
    });

    mapButton.addEventListener('click', function() {
        detailInfo.style.display = 'none';
        mapInfo.style.display = 'block';
        reviewInfo.style.display = 'none';
    });

    reviewButton.addEventListener('click', function() {
        detailInfo.style.display = 'none';
        mapInfo.style.display = 'none';
        reviewInfo.style.display = 'block';
    });


//    function showMap() {
//        const locationElement = document.querySelector('.info-location');
//        const location = locationElement ? locationElement.innerText.trim() : 'default location'; // 공연 장소 정보를 가져옴
//        console.log('Place name to search:', location);
//        const largeCard = document.querySelector('.large-card .related-info .content');
//        largeCard.innerHTML = '<div id="map" style="width: 100%; height: 400px;"></div>';
//        initMap(location);
//    }


//    document.addEventListener("DOMContentLoaded", function() {
//        document.getElementById("showMapBtn").addEventListener("click", function() {
//            showMap();
//        });
//
//        document.getElementById("showReviewsBtn").addEventListener("click", function() {
//            showReviews();
//        });
//
//        document.getElementById("reviewForm").addEventListener("submit", async function(event) {
//            event.preventDefault();
//            await submitReview();
//        });
//    });

});