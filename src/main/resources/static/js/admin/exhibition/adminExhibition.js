$(document).ready(

            function(){


                // api table
                function findAPIFestivalList(page){

                    $('#list2').html("");

                    $.ajax({

                        url: '/admin/festival-regulate/findAPIFestivalList?page=' + page,
                        method: 'POST',
                        contentType: 'application/json',
                        success: function(list){

                            $.each(list, function(index, festival){

                                var index1 = (index + 1) + (page-1)*5;


                                var content = festival.festivalContent.length > 30?
                                festival.festivalContent.substring(0,30) + "..."
                                : festival.festivalContent;



                                var htmlContent =`

                                    <tr>

                                        <th scope="row">${index1}</th>
                                        <td id="regionId">${festival.regionId}</td>
                                        <td id="timeId">${festival.timeId}</td>
                                        <td id="festivalName">${festival.festivalName}</td>
                                        <td id="festivalContent">${content}</td>
                                        <td id="manageInstitution">${festival.manageInstitution}</td>
                                        <td id="hostInstitution">${festival.hostInstitution}</td>
                                        <td id="sponserInstitution">${festival.sponserInstitution}</td>
                                        <td id="tel">${festival.tel}</td>
                                        <td id="homepageUrl">${festival.homepageUrl}</td>
                                        <td id="detailAddress">${festival.detailAddress}</td>
                                        <td id="latitude">${festival.latitude}</td>
                                        <td id="longtitude">${festival.longtitude}</td>
                                        <td id="place">${festival.place}</td>
                                        <td id="startDate">${festival.startDate}</td>
                                        <td id="endDate">${festival.endDate}</td>
                                        <td id="avgRate">${festival.avgRate}</td>
                                        <td id="season">${festival.season}</td>
                                        <td id="imgUrl"><img src="${festival.imgUrl}" width="40px"></td>

                                    </tr>

                                `;

                                $('#list2').append(htmlContent);

                            })


                        }

                    })
                }


                findAPIFestivalList(1);

                for(var p=1; p<=10; p++){

                    $('#pageNum2').append(`<button class="pageBtn2">${p}</button>`)


                }

                $(document).on('click','.pageBtn2', function(){

                    const page = $(this).text();
                    findAPIFestivalList(page);

                })
            }
        )