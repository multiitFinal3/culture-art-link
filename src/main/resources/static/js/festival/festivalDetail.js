$(document).ready(
    function(){


        //#region 찜 / 관심없음 관련 함수


        /**
        * 찜하기 / 찜 취소하기 버튼
        *
        */
        $(document).on('click','.heart', function(){

            var button = $(this);
            var festivalId = $(this).val();

            if(button.closest(".card").hasClass("hasLove")){

                $.ajax({

                    url: '/festival/deleteUserLoveFestival?festivalId=' + festivalId,
                    method: 'POST',
                    contentType: 'application/json',
                    success: function(res){
                        alert(res);
                        button.closest('.card').removeClass("hasLove");
                    },
                    error: function(xhr, status, error){

                        alert('error : ',error);

                    }
                })

            }else{

                if(button.closest(".card").hasClass("hasHate")){
                    button.closest(".card").find(".hate").click();
                }


                $.ajax({

                    url: '/festival/insertUserLoveFestival?festivalId=' + festivalId,
                    method: 'POST',
                    contentType: 'application/json',
                    success: function(res){

                        alert(res);
                        button.closest(".card").addClass("hasLove");
                        console.log("addClass('hasLove');")


                    },
                    error: function(xhr, status, error){

                     alert('error : ',error);

                    }

                })


            }

        })


        /**
        * 찜 리스트 찾아서 색 바꾸기
        *
        */
        function findLoveList(){

            var cardList = document.querySelectorAll(".card");

            $.ajax({

                url: '/festival/findLoveList',
                method: 'POST',
                contentType: 'application/json',
                success: function(list){

                    console.log(list);
                    $.each(cardList, function(index, card){

                        console.log("card.getAttribute('value')")
                        console.log(card.getAttribute('value'));

                        var cardString = card.getAttribute('value');
                        var cardInt = parseInt(cardString, 10);

                        if(list.includes(cardInt)){

                            $(card).addClass("hasLove");

                        }
                    })
                }
            })

        }






       /**
        * 관심없음 하기 / 관심없음 취소하기 버튼
        *
        */
        $(document).on('click','.hate', function(){

            var button = $(this);
            var festivalId = $(this).val();

            if(button.closest(".card").hasClass("hasHate")){

                $.ajax({

                    url: '/festival/deleteUserHateFestival?festivalId=' + festivalId,
                    method: 'POST',
                    contentType: 'application/json',
                    success: function(res){
                        alert(res);
                        button.closest('.card').removeClass("hasHate");
                    },
                    error: function(xhr, status, error){

                        alert('error : ',error);

                    }
                })

            }else{

                if(button.closest(".card").hasClass("hasLove")){
                    button.closest(".card").find(".heart").click();
                }

                $.ajax({

                    url: '/festival/insertUserHateFestival?festivalId=' + festivalId,
                    method: 'POST',
                    contentType: 'application/json',
                    success: function(res){

                        alert(res);
                        button.closest(".card").addClass("hasHate");
                        console.log("addClass('hasHate');")


                    },
                    error: function(xhr, status, error){

                     alert('error : ',error);

                    }

                })


            }





        })


        /**
        * 관심없음 리스트 찾아서 색 바꾸기
        *
        */
        function findHateList(){

            var cardList = document.querySelectorAll(".card");

            $.ajax({

                url: '/festival/findHateList',
                method: 'POST',
                contentType: 'application/json',
                success: function(list){

                    console.log(list);
                    $.each(cardList, function(index, card){

                        console.log("card.getAttribute('value')")
                        console.log(card.getAttribute('value'));

                        var cardString = card.getAttribute('value');
                        var cardInt = parseInt(cardString, 10);

                        if(list.includes(cardInt)){

                            $(card).addClass("hasHate");

                        }
                    })
                }
            })

        }



        findLoveList();
        findHateList();

        //#endregion



        //#region 리뷰 관련 함수






        //#endregion



    }
)
