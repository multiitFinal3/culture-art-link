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

        function showMap() {
            const locationElement = document.querySelector('.info-location');
            const location = locationElement ? locationElement.innerText.trim() : 'default location'; // 공연 장소 정보를 가져옴
            console.log('Place name to search:', location);
            const largeCard = document.querySelector('.large-card .related-info .content');
            largeCard.innerHTML = '<div id="map" style="width: 100%; height: 400px;"></div>';
            initMap(location);
        }

        document.addEventListener("DOMContentLoaded", function() {
            document.getElementById("showMapBtn").addEventListener("click", function() {
                showMap();
            });

            document.getElementById("showReviewsBtn").addEventListener("click", function() {
                showReviews();
            });

            document.getElementById("reviewForm").addEventListener("submit", async function(event) {
                event.preventDefault();
                await submitReview();
            });
        });

        async function showReviews() {
            const performanceId = parseInt(document.getElementById("performanceId").value, 10);
            const largeCard = document.querySelector('.large-card .related-info .content');
            largeCard.innerHTML = ''; // 기존 내용을 비웁니다

            try {
                const response = await axios.get(`/reviews/performance/${performanceId}`);
                const reviews = response.data;

                if (reviews.length === 0) {
                    largeCard.innerHTML = "<p>아직 후기가 없습니다. 첫 후기를 작성해보세요!</p>";
                } else {
                    reviews.forEach(review => {
                        const reviewElement = document.createElement('div');
                        reviewElement.classList.add('review');
                        reviewElement.innerHTML = `
                            <p><strong>별점:</strong> ${'★'.repeat(review.starRating)}${'☆'.repeat(5 - review.starRating)}</p>
                            <p>${review.content}</p>
                            <p><em>${new Date(review.reviewDate).toLocaleDateString()}</em></p>
                        `;
                        largeCard.appendChild(reviewElement);
                    });
                }
            } catch (error) {
                console.error('후기를 가져오는 중 오류가 발생했습니다:', error);
                largeCard.innerHTML = "<p>후기를 불러오는 데 실패했습니다.</p>";
            }

            document.querySelector('.review-section').style.display = 'block';
        }

        async function submitReview() {
            const performanceId = parseInt(document.getElementById("performanceId").value, 10);
            const starRating = document.querySelector('input[name="starRating"]:checked').value;
            const content = document.getElementById("reviewContent").value;

            try {
                await axios.post('/reviews', {
                    performanceId: performanceId,
                    starRating: starRating,
                    content: content,
                });
                showReviews(); // 후기를 제출한 후 후기를 다시 불러옵니다
            } catch (error) {
                console.error('후기를 제출하는 중 오류가 발생했습니다:', error);
            }
        }











              document.addEventListener('DOMContentLoaded', function() {
                  // 현재 상태 가져오기
                  axios.get(`/getPerformanceLikeState?userId=${userId}&performanceId=${performanceId}`)
                      .then(response => {
                          let state = response.data.state;  // state 변수를 정의합니다.
                          console.log('Current state:', state);  // 상태값 확인

                          // 상태에 따라 버튼 이미지를 설정
                          if (state === 'like') {
                              document.getElementById('likeBtn').src = 'https://kr.object.ncloudstorage.com/team3/common/upBlue.png';
                              document.getElementById('dislikeBtn').src = 'https://kr.object.ncloudstorage.com/team3/common/downNo.png';
                          } else if (state === 'not like') {
                              document.getElementById('likeBtn').src = 'https://kr.object.ncloudstorage.com/team3/common/upNo.png';
                              document.getElementById('dislikeBtn').src = 'https://kr.object.ncloudstorage.com/team3/common/downRed.png';
                          } else {
                              // 기본 초기 상태
                              document.getElementById('likeBtn').src = 'https://kr.object.ncloudstorage.com/team3/common/upNo.png';
                              document.getElementById('dislikeBtn').src = 'https://kr.object.ncloudstorage.com/team3/common/downNo.png';
                          }
                      })
                      .catch(error => {
                          console.error('오류 발생:', error);
                      });

                  // 좋아요 버튼 클릭 이벤트
                  document.getElementById('likeBtn').addEventListener('click', function() {
                      updatePerformanceState('like');
                  });

                  // 싫어요 버튼 클릭 이벤트
                  document.getElementById('dislikeBtn').addEventListener('click', function() {
                      updatePerformanceState('not like');
                  });

                  // 좋아요 또는 싫어요 상태 업데이트 함수
                  function updatePerformanceState(state) {
                      axios.post('/performance/updatePerformanceLikeState', {
                          userId: userId,
                          performanceId: performanceId,
                          state: state
                      }, {
                          headers: {
                              'Content-Type': 'application/json'
                          }
                      })
                      .then(() => {
                          // 상태에 따라 버튼 이미지를 설정하고 알림창 표시
                          if (state === 'like') {
                              document.getElementById('likeBtn').src = 'https://kr.object.ncloudstorage.com/team3/common/upBlue.png';
                              document.getElementById('dislikeBtn').src = 'https://kr.object.ncloudstorage.com/team3/common/downNo.png';
                              alert('찜이 추가되었습니다.');
                          } else {
                              document.getElementById('likeBtn').src = 'https://kr.object.ncloudstorage.com/team3/common/upNo.png';
                              document.getElementById('dislikeBtn').src = 'https://kr.object.ncloudstorage.com/team3/common/downRed.png';
                              alert('관심없음이 추가되었습니다.');
                          }
                      })
                      .catch(error => {
                          console.error('좋아요/싫어요 상태를 업데이트하는 중 오류가 발생했습니다:', error);
                      });
                  }
              });