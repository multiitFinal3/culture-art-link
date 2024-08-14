$(document).ready(function() {
      loadAllCulturalProperties();

      function loadAllCulturalProperties() {
          $.ajax({
              url: '/cultural-properties/getAll',
              type: 'GET',
              success: function(data) {
                  console.log('데이터 수신 성공:', data);
                  renderCulturalProperties(data);
                  getUserId().then(userId => {
                      loadUserInterest(userId);
                  }).catch(error => {
                      console.error('사용자 ID 요청 중 오류 발생:', error);
                  });
              },
              error: function(xhr, status, error) {
                  console.error('AJAX 요청 실패:', status, error);
              }
          });
      }


function renderCulturalProperties(properties) {
    const gallerySection = $('#gallery-section');
    gallerySection.empty();



    // properties가 undefined이거나 빈 배열인 경우 처리
    if (!properties || properties.length === 0) {
        gallerySection.append('<p class="empty-message">등록된 문화재가 없습니다.</p>'); // 스타일 클래스를 추가
        return;
    }

    properties.forEach(function(property) {
        const item = $('<div>').addClass('all-cultural-property-item');
        const link = $('<a>').attr('href', `/cultural-properties/detail/${property.id}`);
        const card = $('<div>').addClass('all-card');
        const img = $('<img>').attr('src', property.mainImgUrl).attr('alt', property.culturalPropertiesName);
        const info = $('<div>').addClass('all-cultural-property-info');
        const title = $('<div>').addClass('all-title').text(property.culturalPropertiesName);
//        const location = $('<div>').addClass('all-location').text(`${property.region} ${property.district}`);
        const location = $('<div>').addClass('all-location').text((property.region && property.district) ? `${property.region} ${property.district}` : '위치정보가 없습니다').css('margin-bottom', '0');
        const category = $('<div>').addClass('all-category').text(property.categoryName);
        const dynasty = $('<div>').addClass('all-dynasty').text(property.dynasty);


       // 별점 추가
       const ratingWrapper = $('<div>').addClass('rating-wrapper');
       const star = $('<span>').addClass('star').text('★ ');
       const averageRating = $('<span>').addClass('average-rating').text(Number(property.averageRating).toFixed(1));
       ratingWrapper.append(star, averageRating);


//       // 찜과 관심없음 버튼 추가
//       const badgeDiv = $('<div>').addClass('badge-container');
//       const likeBadge = $('<img>').addClass('like-badge').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/upNo.png').attr('alt', '찜');
//       const dislikeBadge = $('<img>').addClass('dislike-badge').attr('src', 'https://kr.object.ncloudstorage.com/team3/common/downNo.png').attr('alt', '관심없음');
//       badgeDiv.append(likeBadge, dislikeBadge);
//       card.append(img, badgeDiv);

// 찜과 관심없음 버튼 추가
const badgeDiv = $('<div>').addClass('badge-container');
const likeButton = $('<button>')
                    .addClass('like-button')
                    .click(function(e) {
                        e.stopPropagation(); // 클릭 이벤트 전파 방지
                        const culturalPropertiesId = cardDiv.data('cultural-properties-id'); // ID 가져오기
                        const isLiked = $(this).data('liked'); // 찜 상태 가져오기

                        if (starBadge.data('disliked')) {
                            // 별이 클릭되어 있으면 클릭 해제
                            starBadge.data('disliked', false); // 관심없음 상태 초기화
                            starBadge.data('starClicked', false); // 클릭 상태 초기화
                            cardDiv.css('background-color', 'white'); // 색상 초기화
                        }

                        if (!isLiked) {
                            // 찜 상태 추가
                            addLike(culturalPropertiesId);
                            $(this).data('liked', true); // 찜 상태 업데이트
                            $(this).data('likeClicked', true); // 찜 클릭 상태 업데이트
                            cardDiv.css('background-color', '#ffe5ea'); // 핑크색으로 변경
                        } else {
                            // 찜 상태 제거
                            removeInterest(culturalPropertiesId, 'LIKE');
                            $(this).data('liked', false); // 찜 상태 업데이트
                            $(this).data('likeClicked', false); // 클릭 상태 초기화
                            cardDiv.css('background-color', 'white'); // 흰색으로 변경
                        }
                    });;

                    // 찜 추가
                    function addLike(culturalPropertiesId) {
                        $.ajax({
                            url: '/cultural-properties/addLike',
                            type: 'POST',
                            data: { culturalPropertiesId: culturalPropertiesId },
                            success: function(response) {
                                console.log('찜 추가 성공:', response);
                                alert(response);
                            },
                            error: function(xhr, status, error) {
                                console.error('찜 추가 실패:', status, error);
                                alert('찜 추가 실패: ' + error);
                            }
                        });
                    }

const dislikeButton = $('<button>').addClass('dislike-button');
badgeDiv.append(likeButton, dislikeButton);
card.append(img, badgeDiv);



//        card.append(img);
        info.append(title, category, location, dynasty, ratingWrapper);
        link.append(card, info);
        item.append(link);
        gallerySection.append(item);
    });

//    addInterestFunctionality();
}


//function renderCulturalProperties(properties) {
//        const gallerySection = $('#gallery-section .row');
//        gallerySection.empty(); // 기존 콘텐츠 초기화
//
//        if (properties.length === 0) {
//            gallerySection.append('<p>등록된 문화재가 없습니다.</p>'); // 데이터가 없을 경우 메시지 추가
//            return;
//        }
//
//        // 각 문화재에 대한 카드 생성
//        $.each(properties, function(index, property) {
//            const colDiv = $('<div>').addClass('col');
//            const cardDiv = $('<div>').addClass('card h-100').css('cursor', 'pointer').click(function() {
//                window.location.href = `/cultural-properties/detail/${property.id}`; // ID에 따라 URL 수정
////            const id = property.id; // 클릭한 카드의 문화재 ID 가져오기
////                    getCulturalPropertyDetail(id); // 문화재 ID를 인자로 전달하여 함수 호출
////                const culturalPropertyId = property.id;
////                getCulturalPropertyDetail(culturalPropertyId);
////                            getCulturalPropertyDetail();
//             }).attr('data-cultural-properties-id', property.id); // ID를 data 속성에 추가
//
//
//
//            const img = $('<img>').addClass('card-img-top')
//                .attr('src', property.mainImgUrl)
//                .attr('alt', property.culturalPropertiesName);
//
//            const cardBodyDiv = $('<div>').addClass('card-body');
//            const category = $('<p>').addClass('card-text').text(property.categoryName).css('margin-bottom', '0');
//            const title = $('<h5>').addClass('card-title').text(property.culturalPropertiesName);
//            const locationText = $('<p>').addClass('card-text').text((property.region && property.district) ? `${property.region} ${property.district}` : '위치정보가 없습니다').css('margin-bottom', '0');
//            const dynasty = $('<p>').addClass('card-text').text(property.dynasty).css('margin-bottom', '0');
//
//
//            // 배지 추가
//            const badgeDiv = $('<div>').addClass('d-flex justify-content-end position-absolute bottom-0 end-0 p-2');
//
//
//            // 찜(하트) 배지
//            const likeBadge = $('<span>')
//                .addClass('badge bg-primary me-1')
//                .text('♥')
//                .css('cursor', 'pointer')
//                .data('liked', false) // 찜 상태 초기화
//                .data('likeClicked', false) // 찜 클릭 상태
//                .click(function(e) {
//                    e.stopPropagation(); // 클릭 이벤트 전파 방지
//                    const culturalPropertiesId = cardDiv.data('cultural-properties-id'); // ID 가져오기
//                    const isLiked = $(this).data('liked'); // 찜 상태 가져오기
//
//                    if (starBadge.data('disliked')) {
//                        // 별이 클릭되어 있으면 클릭 해제
//                        starBadge.data('disliked', false); // 관심없음 상태 초기화
//                        starBadge.data('starClicked', false); // 클릭 상태 초기화
//                        cardDiv.css('background-color', 'white'); // 색상 초기화
//                    }
//
//                    if (!isLiked) {
//                        // 찜 상태 추가
//                        addLike(culturalPropertiesId);
//                        $(this).data('liked', true); // 찜 상태 업데이트
//                        $(this).data('likeClicked', true); // 찜 클릭 상태 업데이트
//                        cardDiv.css('background-color', '#ffe5ea'); // 핑크색으로 변경
//                    } else {
//                        // 찜 상태 제거
//                        removeInterest(culturalPropertiesId, 'LIKE');
//                        $(this).data('liked', false); // 찜 상태 업데이트
//                        $(this).data('likeClicked', false); // 클릭 상태 초기화
//                        cardDiv.css('background-color', 'white'); // 흰색으로 변경
//                    }
//                });
//
//            // 관심없음(별) 배지
//            const starBadge = $('<span>')
//                .addClass('badge bg-secondary')
//                .text('☆')
//                .css('cursor', 'pointer')
//                .data('disliked', false) // 관심없음 상태 초기화
//                .data('starClicked', false) // 관심없음 클릭 상태
//                .click(function(e) {
//                    e.stopPropagation(); // 클릭 이벤트 전파 방지
//                    const culturalPropertiesId = cardDiv.data('cultural-properties-id'); // ID 가져오기
//                    const isDisliked = $(this).data('disliked'); // 관심없음 상태 가져오기
//
//                    if (likeBadge.data('liked')) {
//                        // 하트가 클릭되어 있으면 클릭 해제
//                        likeBadge.data('liked', false); // 찜 상태 초기화
//                        likeBadge.data('likeClicked', false); // 클릭 상태 초기화
//                        cardDiv.css('background-color', 'white'); // 색상 초기화
//                    }
//
//                    if (!isDisliked) {
//                        // 관심없음 상태 추가
//                        addDislike(culturalPropertiesId);
//                        $(this).data('disliked', true); // 상태 업데이트
//                        $(this).data('starClicked', true); // 관심없음 클릭 상태 업데이트
//                        cardDiv.css('background-color', '#dfdfdf'); // 회색으로 변경
//                    } else {
//                        // 관심없음 상태 제거
//                        removeInterest(culturalPropertiesId, 'DISLIKE');
//                        $(this).data('disliked', false); // 상태 업데이트
//                        $(this).data('starClicked', false); // 클릭 상태 초기화
//                        cardDiv.css('background-color', 'white'); // 흰색으로 변경
//                    }
//                });
//
//
//            // 찜 추가
//            function addLike(culturalPropertiesId) {
//                $.ajax({
//                    url: '/cultural-properties/addLike',
//                    type: 'POST',
//                    data: { culturalPropertiesId: culturalPropertiesId },
//                    success: function(response) {
//                        console.log('찜 추가 성공:', response);
//                        alert(response);
//                    },
//                    error: function(xhr, status, error) {
//                        console.error('찜 추가 실패:', status, error);
//                        alert('찜 추가 실패: ' + error);
//                    }
//                });
//            }
//
//
//            // 관심없음 추가
//            function addDislike(culturalPropertiesId) {
//                $.ajax({
//                    url: '/cultural-properties/addDislike',
//                    type: 'POST',
//                    data: { culturalPropertiesId: culturalPropertiesId },
//                    success: function(response) {
//                        console.log('관심없음 추가 성공:', response);
//                        alert(response);
//                    },
//                    error: function(xhr, status, error) {
//                        console.error('관심없음 추가 실패:', status, error);
//                        alert('관심없음 추가 실패: ' + error);
//                    }
//                });
//            }
//
//
//            // 찜, 관심없음 삭제
//            function removeInterest(culturalPropertiesId, interestType) {
//                $.ajax({
//                    url: '/cultural-properties/removeInterest',
//                    type: 'POST',
//                    data: { culturalPropertiesId: culturalPropertiesId, interestType: interestType },
//                    success: function(response) {
//                        console.log('찜이 제거되었습니다:', response);
//
//                        // interestType에 따라 다른 알림 메시지 표시
//                        if (interestType === 'LIKE') {
//                            alert('찜이 취소되었습니다');
//                        } else if (interestType === 'DISLIKE') {
//                            alert('관심없음이 취소되었습니다');
//                        }
//
//                    },
//                    error: function(xhr, status, error) {
//                        console.error('찜 제거 실패:', status, error);
//                        alert('찜 제거 실패: ' + error);
//                    }
//                });
//            }
//
//
//            // 배지 구성
//            badgeDiv.append(likeBadge).append(starBadge);
//
//
//            // 카드 구성
//            cardBodyDiv.append(category).append(title).append(locationText).append(dynasty);
//            cardBodyDiv.append(badgeDiv); // 배지를 카드 본문에 추가
//            cardDiv.append(img).append(cardBodyDiv);
//            colDiv.append(cardDiv);
//            gallerySection.append(colDiv);
//        });
//
//    }
////////////////////////////


//function addInterestFunctionality() {
//    $('.all-card').each(function() {
//        const card = $(this);
//        const culturalPropertiesId = card.data('cultural-property-id');
//        const likeBadge = card.find('.like-badge');
//        const dislikeBadge = card.find('.dislike-badge');
//
//        likeBadge.click(function(e) {
//            e.preventDefault();
//            e.stopPropagation();
//            handleInterestClick(culturalPropertiesId, 'LIKE', card, likeBadge, dislikeBadge);
//        });
//
//        dislikeBadge.click(function(e) {
//            e.preventDefault();
//            e.stopPropagation();
//            handleInterestClick(culturalPropertiesId, 'DISLIKE', card, likeBadge, dislikeBadge);
//        });
//    });
//}
//
//function handleInterestClick(culturalPropertiesId, interestType, card, activeBadge, inactiveBadge) {
//    const isActive = activeBadge.attr('src').includes('Blue') || activeBadge.attr('src').includes('Red');
//
//    if (isActive) {
//        removeInterest(culturalPropertiesId, interestType);
//        activeBadge.attr('src', interestType === 'LIKE' ? 'https://kr.object.ncloudstorage.com/team3/common/upNo.png' : 'https://kr.object.ncloudstorage.com/team3/common/downNo.png');
//    } else {
//        addInterest(culturalPropertiesId, interestType);
//        activeBadge.attr('src', interestType === 'LIKE' ? 'https://kr.object.ncloudstorage.com/team3/common/upBlue.png' : 'https://kr.object.ncloudstorage.com/team3/common/downRed.png');
//        inactiveBadge.attr('src', interestType === 'LIKE' ? 'https://kr.object.ncloudstorage.com/team3/common/downNo.png' : 'https://kr.object.ncloudstorage.com/team3/common/upNo.png');
//    }
//}

/////////////////////////////////////////////////////
//// Like 버튼 이벤트 핸들러
//    $(document).on('click', '.like-button', function(e) {
//        e.preventDefault();
//        e.stopPropagation();
//        handleInterestClick($(this), 'LIKE');
//    });
//
//    // Dislike 버튼 이벤트 핸들러
//    $(document).on('click', '.dislike-button', function(e) {
//        e.preventDefault();
//        e.stopPropagation();
//        handleInterestClick($(this), 'DISLIKE');
//    });
//
//    function handleInterestClick(button, interestType) {
//        const card = button.closest('.all-card');
//        const culturalPropertiesId = card.data('cultural-property-id');
//        const isActive = button.hasClass('active');
//        const action = isActive ? 'remove' : 'add';
//
//        $.ajax({
//            url: `/cultural-properties/${action}Interest`,
//            type: 'POST',
//            data: {
//                culturalPropertiesId: culturalPropertiesId,
//                interestType: interestType
//            },
//            success: function(response) {
//                if (action === 'add') {
//                    button.addClass('active');
//                    // 다른 버튼의 활성 상태 제거
//                    if (interestType === 'LIKE') {
//                        card.find('.dislike-button').removeClass('active');
//                    } else {
//                        card.find('.like-button').removeClass('active');
//                    }
//                } else {
//                    button.removeClass('active');
//                }
//                alert(response); // 서버 응답 메시지 표시
//            },
//            error: function(xhr, status, error) {
//                console.error('Interest update failed:', error);
//                alert('작업 실패: ' + xhr.responseText);
//            }
//        });
//    }
//
//
//function addInterestFunctionality() {
//    $('.all-card').each(function() {
//        const card = $(this);
//        const culturalPropertiesId = card.data('cultural-property-id');
//        const likeButton = card.find('.like-button');
//        const dislikeButton = card.find('.dislike-button');
//
//        likeButton.click(function(e) {
//            e.preventDefault();
//            e.stopPropagation();
//            handleInterestClick(culturalPropertiesId, 'LIKE', card, likeButton, dislikeButton);
//        });
//
//        dislikeButton.click(function(e) {
//            e.preventDefault();
//            e.stopPropagation();
//            handleInterestClick(culturalPropertiesId, 'DISLIKE', card, likeButton, dislikeButton);
//        });
//    });
//}
//////////////////////////////////////////////////////////////////////222222222222222

//      // 이벤트 위임을 사용하여 동적으로 생성된 요소에 대해서도 이벤트가 작동하도록 함
//      $(document).on('click', '.like-button, .dislike-button', function(e) {
//          e.preventDefault();
//          e.stopPropagation();
//
//          const button = $(this);
//          const card = button.closest('.all-card');
//          const culturalPropertiesId = card.data('cultural-property-id');
//          const isLike = button.hasClass('like-button');
//          const isActive = button.hasClass('active');
//
//          if (isActive) {
//              removeInterest(culturalPropertiesId, isLike ? 'LIKE' : 'DISLIKE', button);
//          } else {
//              if (isLike) {
//                  addLike(culturalPropertiesId, button, card.find('.dislike-button'));
//              } else {
//                  addDislike(culturalPropertiesId, button, card.find('.like-button'));
//              }
//          }
//      });
//
//      function addLike(culturalPropertiesId, button, otherButton) {
//      console.log('Sending addLike request with culturalPropertiesId:', culturalPropertiesId);
//          $.ajax({
//              url: '/cultural-properties/addLike',
//              type: 'POST',
//              data: { culturalPropertiesId: culturalPropertiesId },
//              success: function(response) {
//                  console.log('찜 추가 성공:', response);
//                  button.addClass('active');
//                  otherButton.removeClass('active');
//                  alert(response);
//              },
//              error: function(xhr, status, error) {
//                  console.error('찜 추가 실패:', status, error);
//                  button.removeClass('active');
//                  alert('찜 추가 실패: ' + (xhr.responseText || error));
//              }
//          });
//      }
//
//      function addDislike(culturalPropertiesId, button, otherButton) {
//          $.ajax({
//              url: '/cultural-properties/addDislike',
//              type: 'POST',
//              data: { culturalPropertiesId: culturalPropertiesId },
//              success: function(response) {
//                  console.log('관심없음 추가 성공:', response);
//                  button.addClass('active');
//                  otherButton.removeClass('active');
//                  alert(response);
//              },
//              error: function(xhr, status, error) {
//                  console.error('관심없음 추가 실패:', status, error);
//                  button.removeClass('active');
//                  alert('관심없음 추가 실패: ' + (xhr.responseText || error));
//              }
//          });
//      }
//
//      function removeInterest(culturalPropertiesId, interestType, button) {
//          $.ajax({
//              url: '/cultural-properties/removeInterest',
//              type: 'POST',
//              data: { culturalPropertiesId: culturalPropertiesId, interestType: interestType },
//              success: function(response) {
//                  console.log('관심 제거 성공:', response);
//                  button.removeClass('active');
//                  alert(interestType === 'LIKE' ? '찜이 취소되었습니다' : '관심없음이 취소되었습니다');
//              },
//              error: function(xhr, status, error) {
//                  console.error('관심 제거 실패:', status, error);
//                  alert('관심 제거 실패: ' + (xhr.responseText || error));
//              }
//          });
//      }

//////////////////////////////////////////////////////


// $('.all-card img').next('.badge-container').remove();



//
//function handleInterestClick(culturalPropertiesId, interestType, card, activeButton, inactiveButton) {
//    const isActive = activeButton.hasClass('active');
//
//    if (isActive) {
//        removeInterest(culturalPropertiesId, interestType);
//        activeButton.removeClass('active');
//    } else {
//        addInterest(culturalPropertiesId, interestType);
//        activeButton.addClass('active');
//        inactiveButton.removeClass('active');
//    }
//}
//
//function addInterest(culturalPropertiesId, interestType) {
//    $.ajax({
//        url: '/cultural-properties/addInterest',
//        type: 'POST',
//        data: { culturalPropertiesId: culturalPropertiesId, interestType: interestType },
//        success: function(response) {
//            console.log(interestType + ' 추가 성공:', response);
//            alert(response);
//        },
//        error: function(xhr, status, error) {
//            console.error(interestType + ' 추가 실패:', status, error);
//            alert(interestType + ' 추가 실패: ' + error);
//        }
//    });
//}
//
//function removeInterest(culturalPropertiesId, interestType) {
//    $.ajax({
//        url: '/cultural-properties/removeInterest',
//        type: 'POST',
//        data: { culturalPropertiesId: culturalPropertiesId, interestType: interestType },
//        success: function(response) {
//            console.log(interestType + ' 제거 성공:', response);
//            alert(interestType + '가 취소되었습니다');
//        },
//        error: function(xhr, status, error) {
//            console.error(interestType + ' 제거 실패:', status, error);
//            alert(interestType + ' 제거 실패: ' + error);
//        }
//    });
//}





//// renderCulturalProperties 함수 내부에 호출 추가
//function renderCulturalProperties(properties) {
//    // ...
//    addInterestFunctionality();
//}

//addInterestFunctionality();

//// 페이지 로드 시 찜 기능 활성화
//$(document).ready(function() {
//    addInterestFunctionality();
//});




//      function addInterestFunctionality() {
//          $('.card').each(function() {
//              const card = $(this);
//              const culturalPropertiesId = card.data('cultural-property-id');
//
//              const badgeDiv = $('<div>').addClass('badge-container');
//              const likeBadge = $('<span>').addClass('badge bg-primary me-1').text('♥').css('cursor', 'pointer');
//              const starBadge = $('<span>').addClass('badge bg-secondary').text('☆').css('cursor', 'pointer');
//
//              badgeDiv.append(likeBadge, starBadge);
//              card.append(badgeDiv);
//
//              likeBadge.click(function(e) {
//                  e.preventDefault();
//                  e.stopPropagation();
//                  handleInterestClick(culturalPropertiesId, 'LIKE', card, likeBadge, starBadge);
//              });
//
//              starBadge.click(function(e) {
//                  e.preventDefault();
//                  e.stopPropagation();
//                  handleInterestClick(culturalPropertiesId, 'DISLIKE', card, likeBadge, starBadge);
//              });
//          });
//      }
//
//      function handleInterestClick(culturalPropertiesId, interestType, card, activeBadge, inactiveBadge) {
//          const isActive = activeBadge.data(interestType === 'LIKE' ? 'liked' : 'disliked');
//
//          if (isActive) {
//              removeInterest(culturalPropertiesId, interestType);
//              activeBadge.data(interestType === 'LIKE' ? 'liked' : 'disliked', false);
//              card.css('background-color', 'white');
//          } else {
//              addInterest(culturalPropertiesId, interestType);
//              activeBadge.data(interestType === 'LIKE' ? 'liked' : 'disliked', true);
//              inactiveBadge.data(interestType === 'LIKE' ? 'disliked' : 'liked', false);
//              card.css('background-color', interestType === 'LIKE' ? '#ffe5ea' : '#dfdfdf');
//          }
//      }
//
//      function addInterest(culturalPropertiesId, interestType) {
//          $.ajax({
//              url: '/cultural-properties/addInterest',
//              type: 'POST',
//              data: { culturalPropertiesId: culturalPropertiesId, interestType: interestType },
//              success: function(response) {
//                  console.log(interestType + ' 추가 성공:', response);
//                  alert(response);
//              },
//              error: function(xhr, status, error) {
//                  console.error(interestType + ' 추가 실패:', status, error);
//                  alert(interestType + ' 추가 실패: ' + error);
//              }
//          });
//      }
//
//      function removeInterest(culturalPropertiesId, interestType) {
//          $.ajax({
//              url: '/cultural-properties/removeInterest',
//              type: 'POST',
//              data: { culturalPropertiesId: culturalPropertiesId, interestType: interestType },
//              success: function(response) {
//                  console.log(interestType + ' 제거 성공:', response);
//                  alert(interestType + '가 취소되었습니다');
//              },
//              error: function(xhr, status, error) {
//                  console.error(interestType + ' 제거 실패:', status, error);
//                  alert(interestType + ' 제거 실패: ' + error);
//              }
//          });
//      }

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

      function loadUserInterest(userId) {
          $.ajax({
              url: '/cultural-properties/getInterest',
              type: 'GET',
              data: { userId: userId },
              success: function(data) {
                  data.forEach(function(item) {
                      const card = $(`.card[data-cultural-property-id="${item.culturalPropertiesId}"]`);
                      if (card.length) {
                          if (item.interestType === 'LIKE') {
                              card.css('background-color', '#ffe5ea');
                              card.find('.badge.bg-primary').data('liked', true);
                          } else if (item.interestType === 'DISLIKE') {
                              card.css('background-color', '#dfdfdf');
                              card.find('.badge.bg-secondary').data('disliked', true);
                          }
                      }
                  });
              },
              error: function(xhr, status, error) {
                  console.error('찜 정보 로드 실패:', status, error);
              }
          });
      }



    // 검색 버튼 클릭 이벤트 핸들러
    $('#searchButton').click(function(e) {
        e.preventDefault(); // 기본 이벤트 제거

        var formData = $('#searchForm').serialize(); // 폼 데이터 직렬화

        $.ajax({
            type: 'GET',
            url: '/cultural-properties/searchMain', // 컨트롤러 매핑 경로
            data: formData,
            success: function(response) {
                // 검색 결과를 처리하는 로직을 여기에 작성
                console.log(response); // 검색 결과 콘솔에 로그

                // response가 배열인 경우
                const properties = response; // 응답을 직접 배열로 사용
                renderCulturalProperties(properties); // 검색 결과를 전달하여 함수 호출


                // 찜 정보를 사용자 ID로 불러오기
                getUserId().then(userId => {
                    loadUserInterest(userId); // 찜 상태 로드
                }).catch(error => {
                    console.error('사용자 ID 요청 중 오류 발생:', error);
                });
            },
            error: function(xhr, status, error) {
                console.error('Ajax 오류 발생: ', error);

            }
        });
    });

      // 초기화 버튼 클릭 이벤트 핸들러
      $('#resetButton').click(function(e) {
          e.preventDefault();
          $('#searchForm')[0].reset();
          loadAllCulturalProperties();
      });



      const genreItems = document.querySelectorAll('.genre-item');
      const tabPanes = document.querySelectorAll('.tab-pane');

      genreItems.forEach(item => {
          item.addEventListener('click', function() {
              const target = this.getAttribute('data-target');

              genreItems.forEach(item => item.classList.remove('active'));
              this.classList.add('active');

              tabPanes.forEach(pane => pane.classList.remove('show', 'active'));
              document.getElementById(target).classList.add('show', 'active');

              if (target === 'news') {
                  loadNewsArticles();
              } else if (target === 'video') {
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
              const link = $('<a>').attr('href', article.link).addClass('btn btn-primary btn-sm mt-2').text('자세히 보기').css('margin-left', '2rem');

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

//      // 페이지 로드 시 뉴스와 비디오 로드
//      loadNewsArticles();
//      loadVideos();
  });