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

    // 좋아요, 싫어요 버튼 이벤트 리스너
    const likeBtn = document.querySelector('.like-btn');
    const dislikeBtn = document.querySelector('.dislike-btn');

    likeBtn.addEventListener('click', function() {
        updateInterest('like');
    });

    dislikeBtn.addEventListener('click', function() {
        updateInterest('dislike');
    });

    function updateInterest(action) {
        const culturalPropertiesId = window.location.pathname.split('/').slice(-2, -1)[0]; // URL에서 ID 추출

        fetch(`/cultural-properties/detail/${culturalPropertiesId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
                'action': action
            })
        })
        .then(response => {
            if (response.ok) {
                return response.json(); // JSON 응답 처리
            }
            throw new Error('Network response was not ok.');
        })
        .then(data => {
            // 버튼 상태 업데이트
            if (action === 'like') {
                likeBtn.classList.add('liked');
                dislikeBtn.classList.remove('disliked'); // 필요 시 상태 초기화
            } else {
                dislikeBtn.classList.add('disliked');
                likeBtn.classList.remove('liked'); // 필요 시 상태 초기화
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
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



