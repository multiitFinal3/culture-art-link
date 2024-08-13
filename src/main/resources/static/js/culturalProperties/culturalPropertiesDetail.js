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
    const culturalPropertiesId = urlParts[urlParts.length - 3]; // 문화재 ID를 URL에서 가져옴

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
        const culturalPropertiesId = urlParts[urlParts.length - 3];

        $.ajax({
            url: `/cultural-properties/detail/${culturalPropertiesId}/review/reviewList?page=${page}`,
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
                                    <img src="${review.userProfileImage || '/img/festival/noPhoto.png'}" alt="${review.userName}">
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
                                <div class="review-actions" style="display: ${review.userId == userId1 ? 'block' : 'none'};"> <button type="button" id="edit-btn-${review.id}" data-id="${review.id}">수정</button> <button type="button" id="delete-btn-${review.id}" data-id="${review.id}" data-cultural-id="${review.culturalPropertiesId}">삭제</button>

                                <button type="button" id="save-btn-${review.id}" style="display:none;" data-id="${review.id}" data-cultural-id="${review.culturalPropertiesId}">저장</button>
                                <button type="button" id="cancel-btn-${review.id}" style="display:none;">취소</button>
                                </div>
                            </div>
                            `;
                        $('.reviews').append(reviewHtml);
                    });

                    // 총 리뷰 개수 및 페이지 업데이트
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
                        $(this).css('color', 'gold'); // 노란색으로 설정
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
                $(this).css('color', 'gold'); // 노란색으로 설정
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

});