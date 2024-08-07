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


   function deleteReview(id, culturalPropertiesId) {
       // 삭제 확인을 위한 알림창
       if (confirm("정말 이 리뷰를 삭제하시겠습니까?")) {
           $.ajax({
               url: `/cultural-properties/detail/${culturalPropertiesId}/review/delete?id=${id}`,
               type: 'DELETE',
               success: function(response) {
                   alert("리뷰가 삭제되었습니다.");
                   // 페이지 리로드 또는 리뷰 목록 갱신 코드 추가
                   location.reload(); // 전체 페이지를 새로고침
               },
               error: function(xhr, status, error) {
                   console.error('리뷰 삭제 실패:', status, error);
                   alert("리뷰 삭제에 실패했습니다.");
               }
           });
       }
   }


});


