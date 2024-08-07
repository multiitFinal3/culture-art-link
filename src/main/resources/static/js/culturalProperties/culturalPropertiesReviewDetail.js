document.addEventListener("DOMContentLoaded", function() {
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
                url: `/cultural-properties/detail/${culturalPropertiesId}/review/getReviewUserId`, // API 요청 URL
                type: 'GET',
                success: function(response) {
                    const userId = response.userId; // 사용자 ID 추출
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


   // 삭제 버튼 클릭 이벤트 설정
   $(document).on('click', '[id^="delete-btn-"]', function(e) {
       e.preventDefault(); // 기본 링크 동작 방지
       const id = $(this).data('id'); // 버튼에서 리뷰 ID 가져오기
       const culturalPropertiesId = $(this).data('cultural-id'); // 버튼에서 문화재 ID 가져오기

       // 삭제할 리뷰 ID와 문화재 ID 출력

       console.log('문화재 ID는:' +  culturalPropertiesId);
       console.log('리뷰 ID는는:' +  id);

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


   // 리뷰 수정 버튼 클릭 이벤트
      $('[id^="edit-btn-"]').on('click', function() {
          const reviewDiv = $(this).closest('.review'); // 수정 버튼이 포함된 리뷰 Div
          const contentTextarea = reviewDiv.find('textarea'); // 텍스트 영역
          const reviewText = reviewDiv.find('.review-text'); // 기존 리뷰 텍스트

          const reviewId = $(this).data('id'); // 버튼에서 리뷰 ID 가져오기
          const culturalId = $(this).data('cultural-id'); // 버튼에서 문화재 ID 가져오기

          // 텍스트 영역 보이기
          contentTextarea.show();
          $(this).hide(); // 수정 버튼 숨기기
          reviewDiv.find('[id^="save-btn-"]').show(); // 저장 버튼 보이기
          reviewDiv.find('[id^="cancel-btn-"]').show(); // 취소 버튼 보이기
          reviewText.hide(); // 기존 리뷰 텍스트 숨기기

          // 별점 클릭 가능하게
          reviewDiv.addClass('review-editable');

          // 별점 클릭 이벤트 추가
          reviewDiv.find('.review-star').off('click').on('click', function() {
              const currentStarIndex = $(this).index(); // 클릭한 별의 인덱스
              const isFilledStar = $(this).html().trim() === '&#9733;'; // 클릭한 별이 노란색 별인지 확인

              // 클릭한 별이 노란색 별일 때
              if (isFilledStar) {
                  // 클릭한 별과 이전의 별을 빈 별로 변경
                  reviewDiv.find('.review-star').each(function(index) {
                      if (index <= currentStarIndex) {
                          $(this).html('&#9734;'); // 빈 별로 변경
                          $(this).css('color', 'lightgray'); // 회색으로 설정
                      }
                  });
                  reviewDiv.data('selected-star', currentStarIndex); // 선택된 별점 업데이트
              } else {
                  // 클릭한 별이 회색 빈 별일 때
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
      $('[id^="save-btn-"]').on('click', function() {
          const reviewDiv = $(this).closest('.review'); // 리뷰 Div 선택
          const contentTextarea = reviewDiv.find('textarea'); // 텍스트 영역
          const reviewId = $(this).data('id'); // 리뷰 ID 가져오기
          const culturalId = $(this).data('cultural-id'); // 문화재 ID 가져오기
          const newContent = contentTextarea.val(); // 수정된 내용
          const newStar = reviewDiv.data('selected-star'); // 클릭된 별점 가져오기

          // AJAX 요청
          $.ajax({
              url: `/cultural-properties/detail/${culturalId}/review/update?id=${reviewId}`, // 요청 URL
              type: 'PUT',
              contentType: 'application/json',
              data: JSON.stringify({ content: newContent, star: newStar }), // 수정된 내용 전송
              success: function(response) {
                  alert(response); // 수정 성공 메시지

                  // 수정된 내용을 DOM에 반영
                  reviewDiv.find('.review-text').text(newContent).show(); // 텍스트 보이기

                  // 별점 업데이트
                  reviewDiv.find('.review-rating').attr('data-rating', newStar); // 데이터 속성 업데이트

                  // 별점 시각적 업데이트
                  updateStarDisplay(reviewDiv, newStar);

                  contentTextarea.hide(); // 텍스트 영역 숨기기
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
   $('[id^="cancel-btn-"]').on('click', function() {
       const reviewDiv = $(this).closest('.review'); // 리뷰 Div 선택
       const contentTextarea = reviewDiv.find('textarea'); // 텍스트 영역
       const reviewText = reviewDiv.find('.review-text'); // 기존 리뷰 텍스트

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


});












