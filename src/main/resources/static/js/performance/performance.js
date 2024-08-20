//  공연 홈 메인 페이지
document.addEventListener('DOMContentLoaded', function() {
    const genreItems = document.querySelectorAll('.genre-item');
    const genreItems2 = document.querySelectorAll('.genre-item2');
    const locationItems = document.querySelectorAll('.location-item');
    const currentGenre = new URLSearchParams(window.location.search).get('genre') || '추천';


    const selectedGenreInput = document.getElementById('selectedGenre');


    // 장르 아이템의 활성화 상태 설정
    genreItems.forEach(item => {
        if (item.textContent.trim() === currentGenre) {
            item.classList.add('active');
        } else {
            item.classList.remove('active');
        }
    });

    genreItems2.forEach(item => {
        const genre2 = new URLSearchParams(window.location.search).get('genre') || '전체';
        if (item.textContent.trim() === genre2) {
            item.classList.add('active');
        } else {
            item.classList.remove('active');
        }
    });

    genreItems.forEach(item => {
        item.addEventListener('click', function() {
            const genre = this.textContent.trim();
            window.location.href = `/performance/performance-genre?genre=${encodeURIComponent(genre)}`;
        });
    });

    genreItems2.forEach(item => {
        item.addEventListener('click', function() {
            const genre = this.textContent.trim();
            window.location.href = `/performance/performanceRanking?genre=${encodeURIComponent(genre)}`;
        });
    });

    // 랭킹 클릭 이벤트 리스너 추가
    const rankingItem = document.getElementById('ranking-item');
    if (rankingItem) {
        rankingItem.addEventListener('click', function() {
            window.location.href = `/performance/performanceRanking`;
        });
    }

    // 지역별 클릭 이벤트 리스너 추가
    const regionItem = document.getElementById('region-item');
    if (regionItem) {
        regionItem.addEventListener('click', function() {
            window.location.href = `/performance/performanceLocation`;
        });
    }

    // 공연목록에서 상세페이지로 이동
    // 공연 카드 클릭 이벤트 추가
    const allPerformanceCards = document.querySelectorAll('.all-card');

    allPerformanceCards.forEach(card => {
        card.addEventListener('click', function() {
            const performanceCode = this.getAttribute('data-performance-code');
            const source = this.getAttribute('data-source');

            window.location.href = `/performance/performanceDetail?performanceCode=${encodeURIComponent(performanceCode)}&source=${encodeURIComponent(source)}`;
        });
    });




    // 장르별 랭킹에서 공연명 클릭 시 상세페이지로 이동
    const ranking5 = document.querySelectorAll('.card');

    ranking5.forEach(card => {
        card.addEventListener('click', function() {
            const performanceTitle = this.getAttribute('data-performance-title');
            const source = this.getAttribute('data-source');

            // AJAX 요청으로 공연명을 통해 공연 코드를 얻음
            fetch(`/performance/getPerformanceCode?performanceTitle=${encodeURIComponent(performanceTitle)}`)
                .then(response => response.json())
                .then(data => {
                    const performanceCode = data.performanceCode;
                    window.location.href = `/performance/performanceDetail?performanceCode=${encodeURIComponent(performanceCode)}&source=${encodeURIComponent(source)}`;
                })
                .catch(error => {
                    console.error('Error fetching performance code:', error);
                });
        });
    });





    // 랭킹 카드 클릭 이벤트 추가 -> 랭킹별 페이지에서 디테일페이지
        const rankingCards = document.querySelectorAll('.ranking-card');

            rankingCards.forEach(card => {
                card.addEventListener('click', function() {
                    const performanceCode = this.getAttribute('data-performance-code');
                    console.log("Performance Code:", performanceCode); // 디버그용 출력
                    const source = this.getAttribute('data-source');
                    if (performanceCode) {
                        window.location.href = `/performance/performanceDetail?performanceCode=${encodeURIComponent(performanceCode)}&source=${encodeURIComponent(source)}`;
                    } else {
                        console.error("Error: Performance code is null");
                    }
                });
            });











    // 지역별 페이지에서 지역 클릭시
    // 지역 아이템 클릭 시 동작 설정
    locationItems.forEach(item => {
        item.addEventListener('click', function() {
            const location = this.textContent.trim();
            let locationCode = '';
            switch(location) {
                case '전체': locationCode = ''; break;
                case '서울': locationCode = '11'; break;
                case '경기': locationCode = '41'; break;
                case '인천': locationCode = '28'; break;
                case '대전': locationCode = '30'; break;
                case '충청': locationCode = '43|44'; break;
                case '강원': locationCode = '51'; break;
                case '부산': locationCode = '26'; break;
                case '대구': locationCode = '27'; break;
                case '울산': locationCode = '31'; break;
                case '경상': locationCode = '47|48'; break;
                case '광주': locationCode = '29'; break;
                case '전라': locationCode = '45|46'; break;
                case '제주': locationCode = '50'; break;
            }
            console.log("선택한 지역: " + location + ", 코드: " + locationCode); // 디버그 라인
            window.location.href = `/performance/performanceLocation?locationCode=${encodeURIComponent(locationCode)}`;
        });
    });





     genreItems.forEach(item => {
         item.addEventListener('click', function() {
             const genre = this.getAttribute('th:data-genre');
             selectedGenreInput.value = genre;
             document.getElementById('searchForm').submit();
         });
     });




    // 검색한 후에 다른 장르페이지 이동이 가능하게
     genreItems.forEach(item => {
         item.addEventListener('click', function() {
             const genre = this.textContent.trim();
             window.location.href = `/performance/performance-genre?genre=${encodeURIComponent(genre)}&keyword=`;
         });
     });















     // 추가 코드: 추천 공연 목록 처리
         const recommendationContainer = document.querySelector('.recommend-items');

         if (recommendationContainer) {
             const recommendationCards = recommendationContainer.querySelectorAll('.all-card');

             recommendationCards.forEach(card => {
                 card.addEventListener('click', function() {
                     const performanceCode = this.getAttribute('data-performance-code');
                     console.log("Recommendation card clicked:", performanceCode);  // 로그 추가
                     window.location.href = `/performance/performanceDetail?performanceCode=${encodeURIComponent(performanceCode)}&source=db`;
                 });
             });
         }


});
