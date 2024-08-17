document.addEventListener("DOMContentLoaded", function() {
    loadReviews(); // 페이지 로드 시 리뷰 로드
    setupEventListeners(); // 이벤트 리스너 설정
    checkLikeState(); // 초기 좋아요/싫어요 상태 확인

    // 별점 선택 이벤트 설정
    document.querySelectorAll('.star-rating .star').forEach(star => {
        star.addEventListener('click', function() {
            const ratingValue = parseInt(this.getAttribute('data-value')); // 선택한 별의 값을 가져옴

            // 모든 별의 'selected' 클래스를 제거
            document.querySelectorAll('.star-rating .star').forEach(s => {
                s.classList.remove('selected');
            });

            // 선택된 별까지 'selected' 클래스를 추가 (왼쪽에서 오른쪽으로)
            let currentStar = this;
            while (currentStar) {
                currentStar.classList.add('selected');
                currentStar = currentStar.nextElementSibling; // 이전 별로 이동
            }

            // 선택된 별의 값을 hidden input에 설정 (반전 값 저장)
            document.getElementById('selectedRating').value = 6 - ratingValue;
        });
    });
});






    // 지도 초기화 함수
    async function initMap(location) {
        console.log('Location:', location); // 위치 정보 로깅

        const mapOptions = {
            center: new naver.maps.LatLng(37.3595704, 127.105399),
            zoom: 15
        };
        const mapElement = document.getElementById('map');
        if (!mapElement) {
            console.error("Container map not found.");
            return;
        }

        const map = new naver.maps.Map(mapElement, mapOptions);

        if (!location || location.trim() === '') {
            console.log('No location provided');
            mapElement.innerHTML = "위치 정보가 없습니다.";
            return;
        }

        // 위치 정보 가져오기
        try {
            const response = await axios.get(`/map/naver?query=${location}`);
            console.log('Response data:', response.data); // 응답 데이터 로깅
            const locationInfo = response?.data?.items[0];

            if (!locationInfo) {
                console.error('No results found for location:', location);
                mapElement.innerHTML = "해당 위치를 찾을 수 없습니다.";
                return;
            }

            const address = locationInfo.address;

            // 주소를 좌표로 변환
            naver.maps.Service.geocode({ address: address }, function(status, response) {
                if (status !== naver.maps.Service.Status.OK) {
                    console.error('Geocoding error:', status);
                    mapElement.innerHTML = "좌표 변환에 실패했습니다.";
                    return;
                }

                const latlng = response.result.items[0].point;

                // 좌표로 이동
                map.setCenter(latlng);

                // 해당 좌표에 마크 설정
                new naver.maps.Marker({
                    position: latlng,
                    map: map
                });
            });

        } catch (error) {
            console.error('Error fetching location data:', error);
            mapElement.innerHTML = "위치 정보를 가져오는 중 오류가 발생했습니다.";
        }
    }

    // 리뷰 로드 함수
        async function loadReviews() {
            const performanceId = document.getElementById("performanceId").value;

            try {
                const response = await axios.get(`/reviews/${performanceId}`);
                const reviews = response.data;
                const reviewsContainer = document.querySelector(".reviews");
                reviewsContainer.innerHTML = ""; // 기존 후기를 초기화

                // 리뷰 개수를 표시하기 위한 변수
                let reviewCount = reviews.length;

                // 리뷰 개수 표시 업데이트
                document.getElementById("totalReviewCount").textContent = `${reviewCount}`;

                reviews.forEach(review => {
                    const reviewElement = document.createElement("div");
                    reviewElement.classList.add("review");
                    reviewElement.setAttribute("data-id", review.id);  // data-id 속성 추가


                    console.log("Review ID:", review.id, "Star:", review.starRating);
                        // 여기서 `review.star` 데이터가 제대로 로딩되고 있는지 확인합니다.

                    reviewElement.innerHTML = `
                        <div class="user-info">
                            <img src="${review.userProfileImage ? 'https://kr.object.ncloudstorage.com/team3' + review.userProfileImage : 'https://kr.object.ncloudstorage.com/team3/img/festival/noPhoto.png'}" alt="${review.userName}">
                            <div class="user-details">
                                <span>${review.userName}</span>
                                <span class="review-date" style="font-size: 12px; color: #757575;">${new Date(review.createdAt).toLocaleDateString()}</span>
                                <div class="review-rating" data-rating="${review.starRating}">
                                    ${"&#9733;".repeat(review.starRating)}${"&#9734;".repeat(5 - review.starRating)}
                                </div>
                            </div>
                        </div>
                        <div class="review-content">
                            <p class="review-text">${review.content}</p>
                            <div class="review-text-container">
                                <textarea id="editContent-${review.id}" rows="5" style="display:none;">${review.content}</textarea>
                            </div>
                            <input type="number" id="editStar-${review.id}" min="1" max="5" style="display:none;" value="${review.starRating}" />
                        </div>
                        ${review.userId === userId ? `
                        <div class="review-actions">
                            <button id="edit-btn-${review.id}" data-id="${review.id}" data-cultural-id="${review.performanceId}" onclick="editReview(${review.id})">수정</button>
                            <button id="delete-btn-${review.id}" data-id="${review.id}" data-cultural-id="${review.performanceId}" onclick="deleteReview(${review.id})">삭제</button>
                            <button id="save-btn-${review.id}" style="display:none;" data-id="${review.id}" data-cultural-id="${review.performanceId}" onclick="saveReview(${review.id})">저장</button>
                            <button id="cancel-btn-${review.id}" style="display:none;" onclick="cancelEdit(${review.id})">취소</button>
                        </div>` : ''}
                    `;


                    reviewsContainer.appendChild(reviewElement);
                });
            } catch (error) {
                console.error("There was an error loading the reviews:", error);
            }
        }




    // 리뷰 제출 함수
    async function submitReview() {
        const performanceId = document.getElementById("performanceId").value;
        const starRating = document.getElementById('selectedRating').value;
        const content = document.getElementById("reviewContent").value;

        if (starRating === "0") {
            alert("별점을 선택해 주세요.");
            return;
        }

        try {
            const response = await axios.post('/reviews', {
                performanceId: performanceId,
                starRating: starRating,
                content: content
            });

            if (response.status === 200 || response.status === 201) {
                alert("후기가 성공적으로 저장되었습니다.");
                loadReviews(); // 후기를 다시 로드
                document.getElementById('reviewForm').reset(); // 폼 초기화
            }
        } catch (error) {
            console.error("There was an error submitting the review:", error);
        }
    }

    // 리뷰 수정 함수
    function editReview(reviewId) {
        const reviewElement = document.querySelector(`.review[data-id="${reviewId}"]`);
        if (!reviewElement) {
            console.error(`리뷰 아이템을 찾을 수 없습니다. reviewId: ${reviewId}`);
            return;
        }

        const reviewTextElement = reviewElement.querySelector('.review-text');
        const reviewTextarea = reviewElement.querySelector(`#editContent-${reviewId}`);
        const reviewStarInput = reviewElement.querySelector(`#editStar-${reviewId}`);
        const saveButton = reviewElement.querySelector(`#save-btn-${reviewId}`);
        const cancelButton = reviewElement.querySelector(`#cancel-btn-${reviewId}`);

        // 이미 수정 모드인 경우 return
        if (saveButton.style.display === 'inline-block') {
            return;
        }

        reviewTextElement.style.display = 'none';
        reviewTextarea.style.display = 'block';
        reviewStarInput.style.display = 'block';
        saveButton.style.display = 'inline-block';
        cancelButton.style.display = 'inline-block';
    }

    // 리뷰 저장 함수 - 이중 제출 방지
    async function saveReview(reviewId) {
        const reviewElement = document.querySelector(`.review[data-id="${reviewId}"]`);
        const reviewTextarea = reviewElement.querySelector(`#editContent-${reviewId}`);
        const reviewStarInput = reviewElement.querySelector(`#editStar-${reviewId}`).value;
        const saveButton = reviewElement.querySelector(`#save-btn-${reviewId}`);

        // 버튼 비활성화
        saveButton.disabled = true;

        try {
            const response = await axios.put(`/reviews/${reviewId}`, {
                content: reviewTextarea.value,
                starRating: reviewStarInput
            });

            if (response.status === 200) {
                alert("리뷰가 성공적으로 수정되었습니다.");
                loadReviews(); // 리뷰를 다시 로드
            }
        } catch (error) {
            console.error("리뷰를 수정하는 중 오류가 발생했습니다:", error);
            alert("리뷰 수정 중 오류가 발생했습니다. 다시 시도해 주세요.");
        } finally {
            saveButton.disabled = false;
        }
    }

    // 리뷰 삭제 함수
    async function deleteReview(reviewId) {
        try {
            const confirmed = confirm("정말로 이 후기를 삭제하시겠습니까?");
            if (confirmed) {
                await axios.delete(`/reviews/${reviewId}`);
                alert("후기가 삭제되었습니다.");
                loadReviews(); // 후기를 다시 로드
            }
        } catch (error) {
            console.error("There was an error deleting the review:", error);
        }
    }

    // 리뷰 취소 함수
    function cancelEdit(reviewId) {
        const reviewElement = document.querySelector(`.review[data-id="${reviewId}"]`);
        const reviewTextElement = reviewElement.querySelector('.review-text');
        const reviewTextarea = reviewElement.querySelector(`#editContent-${reviewId}`);
        const reviewStarInput = reviewElement.querySelector(`#editStar-${reviewId}`);
        const saveButton = reviewElement.querySelector(`#save-btn-${reviewId}`);
        const cancelButton = reviewElement.querySelector(`#cancel-btn-${reviewId}`);

        reviewTextElement.style.display = 'block';
        reviewTextarea.style.display = 'none';
        reviewStarInput.style.display = 'none';
        saveButton.style.display = 'none';
        cancelButton.style.display = 'none';
    }

    // 좋아요/싫어요 상태 확인 함수
    async function checkLikeState() {
        try {
            const response = await axios.get(`/performance/getPerformanceLikeState?userId=${userId}&performanceId=${performanceId}`);
            const state = response.data.state;
            console.log('Current state:', state);

            const likeBtn = document.getElementById('likeBtn');
            const dislikeBtn = document.getElementById('dislikeBtn');

            if (state === 'like') {
                likeBtn.src = 'https://kr.object.ncloudstorage.com/team3/common/upBlue.png';
                dislikeBtn.src = 'https://kr.object.ncloudstorage.com/team3/common/downNo.png';
            } else if (state === 'not like') {
                likeBtn.src = 'https://kr.object.ncloudstorage.com/team3/common/upNo.png';
                dislikeBtn.src = 'https://kr.object.ncloudstorage.com/team3/common/downRed.png';
            } else {
                likeBtn.src = 'https://kr.object.ncloudstorage.com/team3/common/upNo.png';
                dislikeBtn.src = 'https://kr.object.ncloudstorage.com/team3/common/downNo.png';
            }
        } catch (error) {
            console.error('오류 발생:', error);
        }
    }


    // 좋아요 또는 싫어요 상태 업데이트 함수
    async function updatePerformanceState(state) {
        try {
            await axios.post('/performance/updatePerformanceLikeState', {
                userId: userId,
                performanceId: performanceId,
                state: state
            }, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });

            // 상태에 따라 버튼 이미지를 설정하고 알림창 표시
            const likeBtn = document.getElementById('likeBtn');
            const dislikeBtn = document.getElementById('dislikeBtn');

            if (state === 'like') {
                likeBtn.src = 'https://kr.object.ncloudstorage.com/team3/common/upBlue.png';
                dislikeBtn.src = 'https://kr.object.ncloudstorage.com/team3/common/downNo.png';
                alert('찜이 추가되었습니다.');
            } else {
                likeBtn.src = 'https://kr.object.ncloudstorage.com/team3/common/upNo.png';
                dislikeBtn.src = 'https://kr.object.ncloudstorage.com/team3/common/downRed.png';
                alert('관심없음이 추가되었습니다.');
            }
        } catch (error) {
            console.error('좋아요/싫어요 상태를 업데이트하는 중 오류가 발생했습니다:', error);
        }
    }

    // 이벤트 리스너 설정 함수
    function setupEventListeners() {
        document.getElementById("showMapBtn").addEventListener("click", function() {
            const locationElement = document.querySelector('.info-location');
            const location = locationElement ? locationElement.innerText.trim() : 'default location'; // 공연 장소 정보를 가져옴
            console.log('Place name to search:', location);
            const largeCard = document.querySelector('.large-card .related-info .content');
            largeCard.innerHTML = '<div id="map" style="width: 100%; height: 400px;"></div>';
            initMap(location);
        });

        document.getElementById("showReviewsBtn").addEventListener("click", function() {
            console.log('showReviewsBtn clicked');
            showReviews();
        });

        document.getElementById("reviewForm").addEventListener("submit", function(event) {
            event.preventDefault();
            submitReview(); // POST 기본값으로 제출
        });

        document.getElementById('likeBtn').addEventListener('click', function() {
            updatePerformanceState('like');
        });

        document.getElementById('dislikeBtn').addEventListener('click', function() {
            updatePerformanceState('not like');
        });
    }

    // 리뷰 표시 함수
    async function showReviews() {
        console.log("showReviews 함수 호출됨");  // 함수 호출 확인
        await loadReviews(); // 후기를 로드

        const relatedInfoContent = document.querySelector('.large-card .related-info .content');
        const reviewSection = document.querySelector('.review-section');

        if (relatedInfoContent && reviewSection) {
            // 기존 관련 정보를 숨기고 후기 섹션을 표시
            relatedInfoContent.innerHTML = ''; // 기존 내용을 비웁니다.
            relatedInfoContent.appendChild(reviewSection); // 후기 섹션을 추가합니다.
            reviewSection.style.display = 'block'; // 리뷰 섹션을 표시
            console.log("리뷰 섹션이 관련 정보 섹션으로 이동했습니다.");
        } else {
            console.error('related-info 또는 review-section을 찾을 수 없습니다.');
        }
    }