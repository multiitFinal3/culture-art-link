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


    function initializeInterestButtons() {
        const urlParts = window.location.pathname.split('/');
            const culturalPropertiesId = urlParts[urlParts.length - 1]; // 마지막 부분을 ID로 사용
        console.log("culturalPropertiesId:"+culturalPropertiesId);

            const likeButton = $('.like-button');
            const dislikeButton = $('.dislike-button');

            likeButton.click(function(e) {
                e.preventDefault();
                handleInterestClick(culturalPropertiesId, 'LIKE', $(this), dislikeButton);
            });

            dislikeButton.click(function(e) {
                e.preventDefault();
                handleInterestClick(culturalPropertiesId, 'DISLIKE', $(this), likeButton);
            });

        getUserId().then(userId => {
            loadUserInterest(userId);
//            loadUserInterest(userId, culturalPropertiesId);
        }).catch(error => {
            console.error('사용자 ID 요청 중 오류 발생:', error);
        });
    }

    function handleInterestClick(culturalPropertiesId, interestType, clickedButton, otherButton) {
        const isActive = clickedButton.hasClass('active');

        if (isActive) {
            removeInterest(culturalPropertiesId, interestType, clickedButton);
        } else {
            if (interestType === 'LIKE') {
                addLike(culturalPropertiesId);
            } else {
                addDislike(culturalPropertiesId);
            }
        }

        // 다른 버튼의 상태 초기화
        otherButton.removeClass('active');
        updateButtonImage(otherButton, false);
    }

    function addLike(culturalPropertiesId) {
        $.ajax({
            url: '/cultural-properties/addLike',
            type: 'POST',
            data: { culturalPropertiesId: culturalPropertiesId },
            success: function(response) {
                console.log('찜 추가 성공:', response);
                alert('찜이 추가되었습니다.');
                updateButtonUI('.like-button', true);
                updateButtonUI('.dislike-button', false);
            },
            error: function(xhr, status, error) {
                console.error('찜 추가 실패:', status, error);
                alert('찜 추가 실패: ' + error);
            }
        });
    }

    function addDislike(culturalPropertiesId) {
        $.ajax({
            url: '/cultural-properties/addDislike',
            type: 'POST',
            data: { culturalPropertiesId: culturalPropertiesId },
            success: function(response) {
                console.log('관심없음 추가 성공:', response);
                alert('관심없음이 추가되었습니다.');
                updateButtonUI('.dislike-button', true);
                updateButtonUI('.like-button', false);
            },
            error: function(xhr, status, error) {
                console.error('관심없음 추가 실패:', status, error);
                alert('관심없음 추가 실패: ' + error);
            }
        });
    }

    function removeInterest(culturalPropertiesId, interestType, clickedButton) {
        $.ajax({
            url: '/cultural-properties/removeInterest',
            type: 'POST',
            data: { culturalPropertiesId: culturalPropertiesId },
            success: function(response) {
                console.log(`${interestType} 제거 성공:`, response);
                alert(`${interestType === 'LIKE' ? '찜이 제거되었습니다.' : '관심없음이 제거되었습니다.'}`);
                updateButtonUI(clickedButton, false);
            },
            error: function(xhr, status, error) {
                console.error(`${interestType} 제거 실패:`, status, error);
                alert(`${interestType} 제거 실패: ` + error);
            }
        });
    }

    function updateButtonUI(button, isActive) {
        const $button = $(button);
        if (isActive) {
            $button.addClass('active');
        } else {
            $button.removeClass('active');
        }
        updateButtonImage($button, isActive);
    }

    function updateButtonImage($button, isActive) {
        const isLikeButton = $button.hasClass('like-button');
        const imagePath = isActive
            ? (isLikeButton ? 'https://kr.object.ncloudstorage.com/team3/common/upBlue.png' : 'https://kr.object.ncloudstorage.com/team3/common/downRed.png')
            : (isLikeButton ? 'https://kr.object.ncloudstorage.com/team3/common/upNo.png' : 'https://kr.object.ncloudstorage.com/team3/common/downNo.png');
        $button.find('img').attr('src', imagePath);
    }


    function getUserId() {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: '/cultural-properties/getUserId',
                type: 'GET',
                success: function(response) {
                    const userId = response.userId;
                    console.log('User ID:', userId);
                    resolve(userId);
                },
                error: function(xhr, status, error) {
                    console.error('사용자 ID 로드 실패:', status, error);
                    reject(error);
                }
            });
        });
    }


    function getUserId() {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: `/cultural-properties/getUserId`,
                type: 'GET',
                success: function(response) {
                    var userId = response.userId; // 사용자 ID 추출
                    userId1 = response.userId;
                    console.log('User ID:', userId);
                    resolve(userId); // ID를 반환
                },
                error: function(xhr, status, error) {
                    console.error('사용자 ID 로드 실패:', status, error);
                    console.error('서버 응답:', xhr.responseText);
                    reject(error); // 에러 발생 시 reject
                }
            });
        });
    }

    $(document).ready(function() {
        getUserId().then(userId => {
            loadUserInterest(userId); // 사용자 ID를 이용해 찜 상태 로드
        }).catch(error => {
            console.error('사용자 ID 요청 중 오류 발생:', error);
        });
    });


    function loadUserInterest(userId) {
        $.ajax({
            url: '/cultural-properties/getInterest',
            type: 'GET',
            data: { userId: userId },
            success: function(interests) {
                // 모든 버튼 비활성화
                updateButtonUI('.like-button', false);
                updateButtonUI('.dislike-button', false);

                // DB에서 가져온 관심 정보에 따라 버튼 활성화
                interests.forEach(function(interest) {
                    if (interest.culturalPropertiesId == culturalPropertiesId) {
                        if (interest.interestType === 'LIKE') {
                            updateButtonUI('.like-button', true);
                        } else if (interest.interestType === 'DISLIKE') {
                            updateButtonUI('.dislike-button', true);
                        }
                    }
                });
            },
            error: function(xhr, status, error) {
                console.error('Failed to load user interests:', error);
            }
        });
    }

    loadUserInterest();
    initializeInterestButtons();






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



    const detailButton = document.querySelector('.button-box');
    const mapButton = document.getElementById('showMapBtn');
    const reviewButton = document.getElementById('showReviewsBtn');
    const detailInfo = document.getElementById('detailInfo');
    const mapInfo = document.getElementById('mapInfo');
    const reviewInfo = document.getElementById('reviewInfo');

    detailButton.addEventListener('click', function() {
        detailInfo.style.display = 'block';
        mapInfo.style.display = 'none';
        reviewInfo.style.display = 'none';

        // 클릭된 버튼 배경색 변경
        $(this).css('background-color', '#7f7f7f');
        $(this).css('color', 'white');

        // 다른 버튼 배경색 원래대로 복원
        $(mapButton).css('background-color', ''); // 기본 색상으로 복원
        $(mapButton).css('color', '');
        $(reviewButton).css('background-color', ''); // 기본 색상으로 복원
        $(reviewButton).css('color', '');

    });

    mapButton.addEventListener('click', function() {
        detailInfo.style.display = 'none';
        mapInfo.style.display = 'block';
        reviewInfo.style.display = 'none';
        initMap();

        // 클릭된 버튼 배경색 변경
        $(this).css('background-color', '#7f7f7f');
        $(this).css('color', 'white');

        // 다른 버튼 배경색 원래대로 복원
        $(detailButton).css('background-color', ''); // 기본 색상으로 복원
        $(detailButton).css('color', '');
        $(reviewButton).css('background-color', ''); // 기본 색상으로 복원
        $(reviewButton).css('color', '');

    });

    reviewButton.addEventListener('click', function() {
        detailInfo.style.display = 'none';
        mapInfo.style.display = 'none';
        reviewInfo.style.display = 'block';

        // 클릭된 버튼 배경색 변경
        $(this).css('background-color', '#7f7f7f');
        $(this).css('color', 'white');

        // 다른 버튼 배경색 원래대로 복원
        $(detailButton).css('background-color', ''); // 기본 색상으로 복원
        $(detailButton).css('color', '');
        $(mapButton).css('background-color', ''); // 기본 색상으로 복원
        $(mapButton).css('color', '');

    });


    function initMap() {
        // 주소 가져오기
        var address = document.getElementById('address').innerText;

        // 위도 및 경도 값
        var propertyData = document.getElementById('property-data');

        if (propertyData) {
            var latitude = parseFloat(propertyData.getAttribute('data-latitude'));
            var longitude = parseFloat(propertyData.getAttribute('data-longitude'));

            // 콘솔에 출력하여 값을 확인합니다.
            console.log('Latitude:', latitude);
            console.log('Longitude:', longitude);

            // Naver Map 생성
            var map = new naver.maps.Map('map', {
                center: new naver.maps.LatLng(latitude || 37.5665, longitude || 126.978), // 기본 위치
                zoom: 15, // 초기 줌 레벨
                mapTypeId: naver.maps.MapTypeId.NORMAL // 지도 타입 설정
            });

            // 마커 위치를 결정할 변수
            var markerPosition;

            if (latitude === 0 && longitude === 0) {
                // 위도와 경도가 0인 경우 주소를 사용하여 위치를 찾음
                var geocoder = new naver.maps.Service.Geocoder();
                geocoder.addressSearch(address, function(status, result) {
                    if (status === naver.maps.Service.Status.OK) {
                        var item = result.v2.addresses[0];
                        markerPosition = new naver.maps.LatLng(item.y, item.x); // 주소로부터 위도와 경도 가져오기

                        // 마커 추가
                        var marker = new naver.maps.Marker({
                            position: markerPosition,
                            map: map,
                            title: address
                        });

                        // 지도 중심 변경
                        map.setCenter(markerPosition);

                        // 정보창 추가
                        var infoWindow = new naver.maps.InfoWindow({
                            content: '<div style="width:150px;text-align:center;">' + address + '</div>'
                        });

                        // 마커 클릭 시 정보창 열기
                        naver.maps.Event.addListener(marker, 'click', function() {
                            infoWindow.open(map, marker);
                        });
                    } else {
                        console.error('주소 검색 실패: ', status);
                    }
                });
            } else {
                // 위도와 경도가 0이 아닌 경우 마커 추가
                markerPosition = new naver.maps.LatLng(latitude, longitude);
                var marker = new naver.maps.Marker({
                    position: markerPosition,
                    map: map,
                    title: address
                });

                // 지도 중심 변경
                map.setCenter(markerPosition);

                // 정보창 추가
                var infoWindow = new naver.maps.InfoWindow({
                    content: '<div style="width:150px;text-align:center;">' + address + '</div>'
                });

                // 마커 클릭 시 정보창 열기
                naver.maps.Event.addListener(marker, 'click', function() {
                    infoWindow.open(map, marker);
                });
            }
        } else {
            console.error('property-data 요소를 찾을 수 없습니다.');
        }
    }





    const imageButton = document.getElementById('imageButton');
    const videoButton = document.getElementById('videoButton');
    const narrationButton = document.getElementById('narrationButton');



    function showImage(imgUrls, imgDescs) {
        const mediaContent = document.getElementById('mediaContent');
        mediaContent.innerHTML = '';
        mediaContent.style.display = 'flex';
        mediaContent.style.flexDirection = 'column';
        mediaContent.style.alignItems = 'center';

        imgUrls.forEach((imgUrl, index) => {
            const imgContainer = document.createElement('div');
            imgContainer.style.position = 'relative';
            imgContainer.style.marginBottom = '20px';
            imgContainer.style.maxWidth = '100%';

            const imgElement = document.createElement('img');
            imgElement.src = imgUrl;
            imgElement.style.width = '100%';
            imgElement.style.maxHeight = '500px';
            imgElement.style.objectFit = 'contain';

            imgContainer.appendChild(imgElement);

            if (index < imgDescs.length) {
                const descElement = document.createElement('div');
                descElement.textContent = imgDescs[index];
                descElement.style.position = 'absolute';
                descElement.style.top = '10px';
                descElement.style.left = '10px';
                descElement.style.backgroundColor = 'rgba(0, 0, 0, 0.7)';
                descElement.style.color = 'white';
                descElement.style.padding = '5px 10px';
                descElement.style.borderRadius = '5px';
                descElement.style.fontSize = '14px';
                imgContainer.appendChild(descElement);
            }

            mediaContent.appendChild(imgContainer);

            imgElement.onerror = function() {
                console.error('Image failed to load:', imgUrl);
                imgContainer.style.display = 'none';
            };
        });

        const mediaModal = new bootstrap.Modal(document.getElementById('mediaModal'));
        mediaModal.show();
    }

    // 이미지 버튼 이벤트 리스너
    if (imageButton) {
        imageButton.addEventListener('click', function() {
            const imgUrls = this.dataset.images.split(',').map(url => url.trim());
            const imgDescs = this.dataset.descriptions.split(',').map(desc => desc.trim());
            showImage(imgUrls, imgDescs);
        });
    }


    function showVideo(videoUrls) {
        const mediaContent = document.getElementById('mediaContent');
        mediaContent.innerHTML = '';
        mediaContent.style.display = 'flex';
        mediaContent.style.flexDirection = 'column';
        mediaContent.style.alignItems = 'center';

        videoUrls.forEach((videoUrl, index) => {
            if (videoUrl && videoUrl.trim() !== '') {
                const videoContainer = document.createElement('div');
                videoContainer.style.width = '100%';
                videoContainer.style.marginBottom = '20px';

                const videoElement = document.createElement('video');
                videoElement.src = videoUrl;
                videoElement.controls = true;
                videoElement.style.width = '100%';
                videoElement.style.maxHeight = '500px';

                const videoTitle = document.createElement('h4');
                videoTitle.textContent = `영상 ${index + 1}`;
                videoTitle.style.marginBottom = '10px';

                videoContainer.appendChild(videoTitle);
                videoContainer.appendChild(videoElement);
                mediaContent.appendChild(videoContainer);
            }
        });

        if (mediaContent.children.length === 0) {
            mediaContent.innerHTML = '<p>표시할 수 있는 비디오가 없습니다.</p>';
        }

        const mediaModal = new bootstrap.Modal(document.getElementById('mediaModal'));
        mediaModal.show();

        // 모달이 닫힐 때 비디오 중지
        document.getElementById('mediaModal').addEventListener('hidden.bs.modal', function () {
            const videos = mediaContent.querySelectorAll('video');
            videos.forEach(video => {
                video.pause();
                video.currentTime = 0;
            });
        });
    }

    // 비디오 버튼 이벤트 리스너
    if (videoButton) {
        videoButton.addEventListener('click', function() {
            const videoUrls = this.dataset.video.split(',').map(url => url.trim());
            showVideo(videoUrls);
            console.log('Video URLs:', videoUrls); // 디버깅용 로그
        });
    }


    function showNarration(narrationUrls) {
        const mediaContent = document.getElementById('mediaContent');
        mediaContent.innerHTML = '';
        mediaContent.style.padding = '20px 0';

        const languages = ['한국어', '영어', '일본어', '중국어'];

        narrationUrls.forEach((url, index) => {
            if (url && url.trim() !== '') {
                const languageDiv = document.createElement('div');
                languageDiv.style.display = 'flex';
                languageDiv.style.alignItems = 'center';
                languageDiv.style.marginBottom = '20px';

                const languageLabel = document.createElement('span');
                languageLabel.textContent = languages[index] + ': ';
                languageLabel.style.width = '60px';
                languageLabel.style.marginRight = '10px';
                languageDiv.appendChild(languageLabel);

                const audioElement = document.createElement('audio');
                audioElement.src = url;
                audioElement.controls = true;
                audioElement.style.flex = '1';
                languageDiv.appendChild(audioElement);

                mediaContent.appendChild(languageDiv);
            }
        });

        if (mediaContent.children.length === 0) {
            mediaContent.innerHTML = '<p>사용 가능한 나레이션이 없습니다.</p>';
        }

        const mediaModal = new bootstrap.Modal(document.getElementById('mediaModal'));
        mediaModal.show();

        // 모달이 닫힐 때 오디오 중지
        document.getElementById('mediaModal').addEventListener('hidden.bs.modal', function () {
            const audioElements = mediaContent.querySelectorAll('audio');
            audioElements.forEach(audio => {
                audio.pause();
                audio.currentTime = 0;
            });
        });
    }

    // 나레이션 버튼 이벤트 리스너
    if (narrationButton) {
        narrationButton.addEventListener('click', function() {
            const narrationUrls = this.dataset.narration.split(',');
            showNarration(narrationUrls);
        });
    }




    var userId1 = 0;

    const stars = document.querySelectorAll(".star");
    let selectedRating = 0; // 선택된 별점 초기화

    // 별 클릭 이벤트 핸들러
    stars.forEach(star => {
        star.addEventListener("click", function() {
//            selectedRating = this.getAttribute("data-value");
            const clickedRating = this.getAttribute("data-value");

            // 클릭한 별이 이미 선택된 별점이면 초기화
            if (clickedRating == selectedRating) {
                selectedRating = 0; // 선택된 별점 초기화
                stars.forEach(s => s.classList.remove("selected")); // 별점 초기화
            } else {
                selectedRating = clickedRating; // 새로운 별점 설정

                // 선택된 별 이하의 별들을 노란색으로 변경
                stars.forEach(s => {
                    s.classList.remove("selected");
                    if (s.getAttribute("data-value") <= selectedRating) {
                        s.classList.add("selected");
                    }
                });
            }
        });
    });

    // 사용자 ID 요청 함수
    function getUserId() {
        return new Promise((resolve, reject) => {
            $.ajax({
                url: `/cultural-properties/detail/${culturalPropertiesId}/review/getReviewUserId`,
                type: 'GET',
                success: function(response) {
                    var userId = response.userId; // 사용자 ID 추출
                    userId1 = response.userId;
//                    console.log("userId1 : ")
//                    console.log(userId1)
                    console.log('User ID:', userId);
                    resolve(userId); // ID를 반환
                },
                error: function(xhr, status, error) {
                    console.error('사용자 ID 로드 실패:', status, error);
                    console.error('서버 응답:', xhr.responseText);
                    reject(error); // 에러 발생 시 reject
                }
            });
        });
    }


// URL에서 문화재 ID 추출
    const urlParts = window.location.pathname.split('/');
    const culturalPropertiesId = urlParts[urlParts.length - 1]; // 문화재 ID를 URL에서 가져옴

    // 리뷰 등록 폼 제출 이벤트 핸들러
    document.getElementById("newReviewForm").addEventListener("submit", function(event) {
        event.preventDefault(); // 기본 제출 방지

        const reviewContent = document.getElementById("reviewContent").value;

        // 별점이 선택되지 않았거나 리뷰 내용이 비어있는 경우
        if (selectedRating === 0) {
            alert("별점을 선택해주세요."); // 별점을 선택하라는 메시지
            return; // 함수 종료
        }

        if (reviewContent.trim() === "") {
            alert("리뷰 내용을 입력해주세요."); // 리뷰 내용 입력하라는 메시지
            return; // 함수 종료
        }

        // 사용자 ID를 받아오는 함수 호출
        getUserId().then(userId => {
            // 리뷰 데이터 생성
            const reviewDTO = {
                culturalPropertiesId: culturalPropertiesId, // 문화재 ID
                userId: userId, // 사용자 ID
                star: selectedRating, // 선택된 별점
                content: reviewContent // 작성한 리뷰 내용
            };
            console.log('리뷰 데이터:', reviewDTO);
            console.log('리뷰 culturalPropertiesId:', culturalPropertiesId);
            console.log('리뷰 User ID:', userId);

            // AJAX 요청
            fetch(`/cultural-properties/detail/${culturalPropertiesId}/review/add`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(reviewDTO) // 리뷰 데이터 JSON 형태로 변환
            })
            .then(response => {
                console.log(response);
                if (response.ok) {
                    alert("리뷰가 등록되었습니다."); // 성공 메시지
                    // 추가: 리뷰 목록을 업데이트하거나 페이지를 리프레시
                    document.getElementById("reviewContent").value = ""; // 리뷰 내용 입력란 초기화
                    stars.forEach(s => s.classList.remove("selected")); // 별점 초기화
                    selectedRating = 0; // 선택된 별점 초기화
                } else {
                    alert("리뷰 등록에 실패했습니다."); // 실패 메시지
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("리뷰 등록 중 오류가 발생했습니다."); // 오류 메시지
            });
        }).catch(error => {
            console.error("Error fetching user ID:", error);
            alert("사용자 ID를 가져오는 데 실패했습니다."); // 오류 메시지
        });
    });


    let currentPage = 0; // 현재 페이지
    let totalPages = 1; // 총 페이지 수

    function loadReviews(page) {
        const urlParts = window.location.pathname.split('/');
        const culturalPropertiesId = urlParts[urlParts.length - 1];

        $.ajax({
            url: `/cultural-properties/detail/${culturalPropertiesId}/reviewList?page=${page}`,
            method: 'GET',
            success: function(response) {
                console.log(response); // 응답 데이터 확인
                if (response && Array.isArray(response.reviews)) {

                    // 총 리뷰 개수
                    const reviewCount = response.totalElements; // 응답에서 총 리뷰 개수 가져오기

                    // 리뷰 리스트 업데이트

                    $('.reviews').empty();

                    var topHtml = `<h2 style="display: inline-block;">리뷰</h2>
                                   <div id="totalReviewCount" class="mt-3" style="display: inline-block; margin-left: 10px;">
                                   <span>${reviewCount}</span>
                                   </div>`;


                   $('.reviews').append(topHtml);

                   // 리뷰가 없는 경우 메시지 추가
                   if (reviewCount === 0) {
                       $('.reviews').append('<br><br><br><br><br><div class="no-reviews" style="font-size: 20px; ">리뷰가 없습니다. 리뷰를 작성해보세요.</div><br><br><br><br><br>');

                       // 페이지 정보를 1/1로 설정
                       $('#currentPage').text('1'); // 현재 페이지
                       $('#totalPages').text('1'); // 총 페이지 수

                       // 버튼 비활성화
                       $('#prevPage').prop('disabled', true);
                       $('#nextPage').prop('disabled', true);
                   } else {

                    // 리뷰를 화면에 출력
                    response.reviews.forEach(review => {

                        var userId = getUserId();

                        // createdAt 값을 Date 객체로 변환
                        const createdAt = new Date(review.createdAt);
                        //(YYYY-MM-DD HH:mm:ss) 형식으로 날짜 포맷팅
                        const formattedDate = createdAt.toISOString().slice(0, 19).replace('T', ' ') + '             ';

                        // updatedAt 포맷팅 (updatedAt이 존재하는 경우)
                        const updatedAtHtml = review.updatedAt && review.updatedAt !== review.createdAt
                            ? ` 수정 : ${new Date(review.updatedAt).toISOString().slice(0, 19).replace('T', ' ')}`
                            : '';

                        const reviewHtml = `
                            <div class="review">
                                <div class="user-info">



                                    <img src="${review.userProfileImage ? 'https://kr.object.ncloudstorage.com/team3' + review.userProfileImage : 'https://kr.object.ncloudstorage.com/team3/img/festival/noPhoto.png'}" alt="${review.userName}">
<!-- <img src="https://kr.object.ncloudstorage.com/team3${review.userProfileImage}" alt="${review.userName}">-->

                                    <div class="user-details">
                                    <span>${review.userName}</span>

                                    <span style="font-size: 13px; color: #999;">
                                        ${formattedDate}   ${updatedAtHtml}
                                    </span>


                                    <div class="review-rating" data-rating="${review.star}">
                                    ${Array.from({ length: 5 }, (_, index) => {
                                        if (index < review.star) {
                                            return `<span class="review-star">&#9733;</span>`;
                                        } else {
                                            return `<span class="review-star empty">&#9734;</span>`;
                                        }
                                    }).join('')}
                                    </div>

                                    </div>
                                </div>
                                <div class="review-content">
                                    <p class="review-content-p-${review.id}">${review.content}</p>
                                    <div class="review-text-container">
                                        <textarea id="editContent-${review.id}" rows="5" style="display:none;" value="${review.content}">${review.content}</textarea>
                                    </div>
                                </div>
                                <div class="review-actions" style="display: ${review.userId == userId1 ? 'block' : 'none'};"> <button type="button" class="btn btn-outline-success" id="edit-btn-${review.id}" data-id="${review.id}">수정</button> <button type="button" class="btn btn-outline-success" id="delete-btn-${review.id}" data-id="${review.id}" data-cultural-id="${review.culturalPropertiesId}">삭제</button>

                                <button type="button" id="save-btn-${review.id}" class="btn btn-outline-success" style="display:none;" data-id="${review.id}" data-cultural-id="${review.culturalPropertiesId}">저장</button>
                                <button type="button" id="cancel-btn-${review.id}" class="btn btn-outline-success" style="display:none;">취소</button>
                                </div>
                            </div>
                            `;
                        $('.reviews').append(reviewHtml);
                    });

                    // 총 리뷰 개수 및 페이지 업데이트
                    $('#totalReviewCount').text(response.totalElements)
                    $('#totalReviewCount span').text(response.totalElements); // 총 리뷰 개수 표시
                    $('#currentPage').text(response.currentPage + 1); // 현재 페이지
                    $('#totalPages').text(response.totalPages); // 총 페이지 수

                    // 페이지 버튼 상태 업데이트
                    $('#prevPage').prop('disabled', response.currentPage === 0);
                    $('#nextPage').prop('disabled', response.currentPage === response.totalPages - 1);

                    // 총 페이지 수 업데이트
                    totalPages = response.totalPages; // 전역 totalPages 업데이트
                    }
                } else {
                    console.error("No reviews found or response format is incorrect");
                }
            },
            error: function(err) {
                console.error('Error loading reviews:', err);
            }
        });
    }



    // 페이지 버튼 클릭 이벤트
    $('#prevPage').click(function() {
        if (currentPage > 0) {
            currentPage--;
            loadReviews(currentPage);
        }
    });

    $('#nextPage').click(function() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadReviews(currentPage);
        }
    });



    // 리뷰 수정 버튼 클릭 이벤트
    $(document).on('click', '[id^="edit-btn-"]', function() {
        const reviewDiv = $(this).closest('.review'); // 수정 버튼이 포함된 리뷰 Div
        const contentTextarea = reviewDiv.find('textarea'); // 텍스트 영역
        const reviewText = reviewDiv.find('.review-content > p'); // 기존 리뷰 텍스트
        const reviewId = $(this).data('id'); // 버튼에서 리뷰 ID 가져오기

        // 기존 리뷰 텍스트 숨기기
        reviewText.hide();

        // 텍스트 영역 보이기
        contentTextarea.show();
        $(this).hide(); // 수정 버튼 숨기기
        reviewDiv.find('[id^="save-btn-"]').show(); // 저장 버튼 보이기
        reviewDiv.find('[id^="cancel-btn-"]').show(); // 취소 버튼 보이기

        // 별점 클릭 가능하게
        reviewDiv.addClass('review-editable');

        // 별점 클릭 이벤트 추가
        reviewDiv.find('.review-star').off('click').on('click', function() {
            const currentStarIndex = $(this).index(); // 클릭한 별의 인덱스
            const isFilledStar = $(this).html().trim() === '&#9733;'; // 클릭한 별이 노란색 별인지 확인

            // 클릭한 별이 노란색 별일 때
            if (isFilledStar) {
                reviewDiv.find('.review-star').each(function(index) {
                    if (index <= currentStarIndex) {
                        $(this).html('&#9734;'); // 빈 별로 변경
                        $(this).css('color', 'lightgray'); // 회색으로 설정
                    }
                });
                reviewDiv.data('selected-star', currentStarIndex); // 선택된 별점 업데이트
            } else {
                reviewDiv.find('.review-star').each(function(index) {
                    if (index <= currentStarIndex) {
                        $(this).html('&#9733;'); // 노란색 별로 변경
                        $(this).css('color', '#ffc107');
//                        $(this).css('color', 'gold'); // 노란색으로 설정
                    } else {
                        $(this).html('&#9734;'); // 빈 별로 변경
                        $(this).css('color', 'lightgray'); // 회색으로 설정
                    }
                });
                reviewDiv.data('selected-star', currentStarIndex + 1); // 선택된 별점 업데이트
            }
        });
    });

    // 저장 버튼 클릭 이벤트
    $(document).on('click', '[id^="save-btn-"]', function() {
        const reviewDiv = $(this).closest('.review'); // 리뷰 Div 선택
        const contentTextarea = reviewDiv.find('textarea'); // 텍스트 영역
        const reviewId = $(this).data('id'); // 리뷰 ID 가져오기
        const culturalId = $(this).data('cultural-id'); // 문화재 ID 가져오기
        const newContent = contentTextarea.val().trim(); // 수정된 내용 (공백 제거)
        const newStar = reviewDiv.data('selected-star'); // 클릭된 별점 가져오기
        const reviewText = reviewDiv.find('.review-content > p'); // 기존 리뷰 텍스트

        // 내용이 비어있으면 경고 메시지 출력하고 종료
        if (newContent === '') {
            alert('내용을 입력해주세요.');
            return;
        }

        // AJAX 요청
        $.ajax({
            url: `/cultural-properties/detail/${culturalId}/review/update?id=${reviewId}`, // 요청 URL
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({ content: newContent, star: newStar || reviewDiv.find('.review-rating').data('rating') }), // 수정된 내용 전송
            success: function(response) {
                alert(response); // 수정 성공 메시지

                // 수정된 내용을 DOM에 반영
                reviewText.text(newContent).show(); // 텍스트 보이기
                contentTextarea.hide(); // 텍스트 영역 숨기기

                // 별점 업데이트
                const updatedStar = newStar || reviewDiv.find('.review-rating').data('rating'); // 수정된 별점 또는 기존 별점
                reviewDiv.find('.review-rating').attr('data-rating', updatedStar); // 데이터 속성 업데이트

                // 별점 시각적 업데이트
                updateStarDisplay(reviewDiv, updatedStar);

                // 저장 후 별점 클릭 이벤트 제거
                reviewDiv.find('.review-star').off('click'); // 별점 클릭 이벤트 제거
                reviewDiv.removeClass('review-editable'); // 별점 클릭 불가능하게 설정

                reviewDiv.data('selected-star', null); // 선택된 별점 초기화
                reviewDiv.find('[id^="edit-btn-"]').show(); // 수정 버튼 보이기
                reviewDiv.find('[id^="save-btn-"]').hide(); // 저장 버튼 숨기기
                reviewDiv.find('[id^="cancel-btn-"]').hide(); // 취소 버튼 숨기기
            },
            error: function(xhr) {
                alert(xhr.responseText); // 오류 메시지
            }
        });
    });

    // 별점 시각적 업데이트 함수
    function updateStarDisplay(reviewDiv, starCount) {
        reviewDiv.find('.review-star').each(function(index) {
            if (index < starCount) {
                $(this).html('&#9733;'); // 채워진 별 표시
                $(this).css('color', '#ffc107');
//                $(this).css('color', 'gold'); // 노란색으로 설정
            } else {
                $(this).html('&#9734;'); // 빈 별 표시
                $(this).css('color', 'lightgray'); // 회색으로 설정
            }
        });
    }

    // 취소 버튼 클릭 이벤트
    $(document).on('click', '[id^="cancel-btn-"]', function() {
        const reviewDiv = $(this).closest('.review'); // 리뷰 Div 선택
        const contentTextarea = reviewDiv.find('textarea'); // 텍스트 영역
        const reviewText = reviewDiv.find('.review-content > p'); // 기존 리뷰 텍스트

        // 기존 내용과 별점 복원
        const originalContent = reviewText.text(); // 기존 리뷰 내용
        const originalStar = reviewDiv.find('.review-rating').data('rating'); // 기존 별점

        // 수정 취소 시
        contentTextarea.val(originalContent); // 텍스트 영역에 기존 내용 설정
        contentTextarea.hide(); // 텍스트 영역 숨기기
        reviewText.show(); // 기존 텍스트 보이기
        updateStarDisplay(reviewDiv, originalStar); // 별점 복원
        reviewDiv.find('[id^="edit-btn-"]').show(); // 수정 버튼 보이기
        reviewDiv.find('[id^="save-btn-"]').hide(); // 저장 버튼 숨기기
        reviewDiv.find('[id^="cancel-btn-"]').hide(); // 취소 버튼 숨기기
    });

    // 페이지 로드 시 리뷰 불러오기
    getUserId();
    loadReviews(0);



    // 삭제 버튼 클릭 이벤트 설정
    $(document).on('click', '[id^="delete-btn-"]', function(e) {
        e.preventDefault(); // 기본 링크 동작 방지
        const id = $(this).data('id'); // 버튼에서 리뷰 ID 가져오기
        const culturalPropertiesId = $(this).data('cultural-id'); // 버튼에서 문화재 ID 가져오기

//        // 삭제할 리뷰 ID와 문화재 ID 출력
//        console.log('문화재 ID는:' +  culturalPropertiesId);
//        console.log('리뷰 ID는는:' +  id);

        // deleteReview 함수 호출
        deleteReview(id, culturalPropertiesId);
    });


    function deleteReview(id, culturalPropertiesId) {
        // 삭제 확인을 위한 알림창
        if (confirm("정말 이 리뷰를 삭제하시겠습니까?")) {
        console.log(`문화재 ID는: ${culturalPropertiesId}`);
        console.log(`리뷰 ID는: ${id}`);
        console.log(`요청 URL: /cultural-properties/detail/${culturalPropertiesId}/review/remove?id=${id}`);
            $.ajax({
                url: `/cultural-properties/detail/${culturalPropertiesId}/review/remove?id=${id}`,
                type: 'DELETE',
                success: function(response) {
                console.log('삭제id:', id);
                console.log("삭제response" + response);
                   alert("리뷰가 삭제되었습니다.");
                   // 페이지 리로드 또는 리뷰 목록 갱신 코드 추가
                   location.reload(); // 전체 페이지를 새로고침
                },
                error: function(xhr, status, error) {
                console.log('삭제에러id:', id);
                   console.error('리뷰 삭제 실패:', status, error);
                   alert("리뷰 삭제에 실패했습니다.");
                }
            });
        }
    }




    const genreItems = document.querySelectorAll('.genre-item');
    const detailContent = document.querySelector('.container');
    const newsContent = document.getElementById('news');
    const videoContent = document.getElementById('video');

    // 초기 상태 설정
    detailContent.style.display = 'block';
    newsContent.style.display = 'none';
    videoContent.style.display = 'none';

    genreItems.forEach(item => {
        item.addEventListener('click', function(e) {
            const target = this.getAttribute('data-target');

            // "전체 문화재 목록" 링크 처리
            if (target === 'all') {
                // 기본 동작 유지 (페이지 이동)
                return;
            }

            e.preventDefault(); // 다른 탭에 대해서는 기본 동작 방지

            // 모든 탭에서 active 클래스 제거
            genreItems.forEach(item => item.classList.remove('active'));
            // 클릭된 탭에 active 클래스 추가
            this.classList.add('active');

            // 모든 콘텐츠 숨기기
            detailContent.style.display = 'none';
            newsContent.style.display = 'none';
            videoContent.style.display = 'none';

            // 선택된 콘텐츠만 표시
            if (target === 'news') {
                newsContent.style.display = 'block';
                loadNewsArticles();
            } else if (target === 'video') {
                videoContent.style.display = 'block';
                loadVideos();
            }
        });
    });


    // 뉴스 로드 함수
    function loadNewsArticles() {
        $.ajax({
            url: '/cultural-properties/news',
            method: 'GET',
            success: function(data) {
              console.log('뉴스 기사를 성공적으로 수신:', data);
              renderNewsArticles(data);
            },
            error: function(xhr, status, error) {
              console.error('AJAX 요청 실패:', status, error);
            }
        });
    }

    // 뉴스 렌더링 함수
    function renderNewsArticles(articles) {
        const newsList = $('#news-list');
        newsList.empty();

        if (!Array.isArray(articles) || articles.length === 0) {
            newsList.append('<li class="list-group-item">등록된 문화재 관련 뉴스가 없습니다.</li>');
            return;
        }

        articles.forEach(function(article) {
            const listItem = $('<li>').addClass('list-group-item');
            if (article.imgUrl) {
              const image = $('<img>').attr('src', article.imgUrl).addClass('img-fluid mb-2');
              listItem.append(image);
            }
            const title = $('<h5>').addClass('mb-1').text(article.title);
            const content = $('<p>').addClass('mb-1').text(article.content);
            const date = $('<small>').addClass('text-muted').text(article.date);
            //              const link = $('<a>').attr('href', article.link).addClass('btn btn-primary btn-sm mt-2').text('자세히 보기').css('margin-left', '2rem');

//            const link = $('<a>').attr('href', article.link).addClass('btn btn-outline-success').text('자세히 보기').css('margin-left', '2rem');

            const link = $('<a>').attr('href', article.link).addClass('btn btn-success').text('자세히 보기').css('margin-left', '2rem');


            listItem.append(title, content, date, link);
            newsList.append(listItem);
        });
    }

    // 비디오 로드 함수
    function loadVideos() {
        const query = "국가유산";
        $.ajax({
            url: '/cultural-properties/videos?query=' + encodeURIComponent(query),
            type: 'POST',
            contentType: 'application/x-www-form-urlencoded',
            success: function(videos) {
              console.log('받은 비디오 데이터:', videos);
              renderVideos(videos);
            },
            error: function(xhr, status, error) {
              console.error('비디오 로드 실패:', status, error);
            }
        });
    }

    // 비디오 렌더링 함수
    function renderVideos(videos) {
        const videoList = $('#video-list');
        videoList.empty();

        if (!Array.isArray(videos) || videos.length === 0) {
            videoList.append('<br><p>등록된 영상이 없습니다.</p>');
            return;
        }

        videos.forEach(function(video) {
            const colDiv = $('<div>').addClass('col-md-4 mb-4');
            const cardDiv = $('<div>').addClass('card');
            const cardBody = $('<div>').addClass('card-body');
            const title = $('<h5>').addClass('card-title').html(video.title);
            const videoId = video.link.split('v=')[1];
            const iframe = $('<iframe>')
                .attr('src', 'https://www.youtube.com/embed/' + videoId)
                .attr('class', 'embed-responsive-item')
                .attr('allowfullscreen', true)
                .css('width', '100%')
                .css('height', '250px');

            cardBody.append(title, iframe);
            cardDiv.append(cardBody);
            colDiv.append(cardDiv);
            videoList.append(colDiv);
        });
    }

});