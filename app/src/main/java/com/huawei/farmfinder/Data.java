package com.huawei.farmfinder;

import com.huawei.hms.site.api.model.Coordinate;
import java.util.ArrayList;
import java.util.Arrays;

public class Data {

    private static Data instance;
    private static ArrayList<Farm> farms;

    private static final String tempDescription = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce pulvinar consectetur dolor at ornare. Vivamus convallis eros congue, aliquet dui sit amet, feugiat velit. Nunc non magna a justo pretium egestas commodo ac nisl. Donec id volutpat velit, eu ultricies turpis. Pellentesque efficitur consectetur massa, vitae volutpat lorem dignissim vitae. Fusce pellentesque, odio et congue volutpat, mi urna sodales est, eu scelerisque ante libero sed nunc. Phasellus faucibus suscipit condimentum. Quisque bibendum rutrum dolor sit amet feugiat.\n" +
            "\n" +
            "Aenean lacinia elit nec mi hendrerit, eget sagittis lorem congue. Mauris vitae mauris dui. Cras turpis lectus, interdum a efficitur et, mollis eget lacus. Donec elit nunc, elementum vitae hendrerit at, sodales eget lacus. Pellentesque id ipsum nunc. Aliquam sit amet posuere felis. Curabitur turpis orci, faucibus et placerat ac, aliquam vitae mi. Ut imperdiet metus mollis aliquet fringilla. Nulla facilisi. Nunc imperdiet purus mauris. Etiam in metus sed nulla bibendum rhoncus.\n" +
            "\n" +
            "Pellentesque a nisi eget sapien feugiat viverra. Mauris vitae sapien quis arcu interdum faucibus eget nec nisl. Curabitur efficitur odio urna, vitae cursus nibh viverra nec. Suspendisse accumsan urna ac justo accumsan, sed aliquet mauris accumsan. Ut eget nisl ut nunc imperdiet interdum nec ut diam. Vestibulum ac facilisis velit, in tempus ipsum. Suspendisse aliquam mauris vel dignissim vulputate. Fusce dapibus auctor fermentum. In consequat porta erat quis scelerisque. Cras bibendum justo et eros blandit, sit amet rutrum arcu scelerisque. Suspendisse ultrices ornare lacus, at tristique ex tincidunt sit amet. Donec ligula justo, tincidunt ultrices lectus et, sollicitudin luctus tortor.";

    private Data() {
        if (null == farms) {
            farms = new ArrayList<>();
            initFarmData();
        }
    }

    public static Data getInstance() {
        if (null == instance) {
            instance = new Data();
        }
        return instance;
    }

    public ArrayList<Farm> getFarms() {
        return farms;
    }

    public Farm getFarmById(int id) {
        for (Farm farm: farms) {
            if (farm.getId() == id) {
                return farm;
            }
        }
        return null;
    }

    private void initFarmData () {
        farms = new ArrayList<>(Arrays.asList(
                new Farm(1, "Fresh Picks",
                        "Daphne Waters",
                        new Coordinate(7.072401314491984, 80.01528869959492),
                        "https://cdn.britannica.com/17/196817-050-6A15DAC3/vegetables.jpg",
                        tempDescription),
                new Farm(2, "Vegies",
                        "Alan Hughes",
                        new Coordinate(7.0914124873373, 79.96548455483774),
                        "https://nhncwtttsf-flywheel.netdna-ssl.com/6/wp-content/uploads/sites/29/2019/01/Eat-Vegetables.jpg",
                        tempDescription),
                new Farm(3, "Fruities",
                        "Danny Brown",
                        new Coordinate(7.165319780569403, 79.91604449496248),
                        "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/assortment-of-colorful-ripe-tropical-fruits-top-royalty-free-image-995518546-1564092355.jpg",
                        tempDescription),
                new Farm(4, "Farm Fresh",
                        "Jane Eyre",
                        new Coordinate(7.222749094277214, 80.02059729533626),
                        "https://www.news-medical.net/image.axd?picture=2020%2F1%2Fshutterstock_321864554.jpg",
                        tempDescription),
                new Farm(5, "Waters Fruits",
                        "Felicity Waters",
                        new Coordinate(7.215820166064452, 80.12112883414713),
                        "https://www.foodsafetynews.com/files/2020/03/fruits-and-vegetables.jpg",
                        tempDescription),
                new Farm(6, "Nice Vegies",
                        "Darrel Kluger",
                        new Coordinate(7.153455051316645, 80.2440947794111),
                        "https://www.nestle.com/sites/default/files/2021-03/related-vegetables-470.jpg",
                        tempDescription),
                new Farm(7, "Fruit Literate",
                        "Sophie Elizabeth",
                        new Coordinate(7.088561090692017, 80.20546950407227),
                        "https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fimg1.cookinglight.timeinc.net%2Fsites%2Fdefault%2Ffiles%2F1502826406%2F1708w-getty-fruit-closeup-CarstenSchanter-EyeEm.jpg",
                        tempDescription),
                new Farm(8, "Veg & Fruit",
                        "John Park",
                        new Coordinate(6.938417885224002, 80.24475718173738),
                        "https://res.cloudinary.com/grohealth/image/upload/v1583748171/DCUK/Content/iStock-857145602.jpg",
                        tempDescription),
                new Farm(9, "Fruit Farm",
                        "Shawn Mendes",
                        new Coordinate(6.892179988349703, 80.5662849067122),
                        "https://www.unlockfood.ca/EatRightOntario/media/Website-images-resized/How-to-store-fruit-to-keep-it-fresh-resized.jpg",
                        tempDescription),
                new Farm(10, "Jones' Joy",
                        "Claudia Jones",
                        new Coordinate(6.9172649216129924, 81.70650305282618),
                        "https://cdn-a.william-reed.com/var/wrbm_gb_food_pharma/storage/images/publications/food-beverage-nutrition/foodnavigator-asia.com/headlines/policy/china-s-tightened-organic-focus-grapes-and-vegetables-top-list-for-mandatory-contaminant-tests/11546195-2-eng-GB/China-s-tightened-organic-focus-Grapes-and-vegetables-top-list-for-mandatory-contaminant-tests.jpg",
                        tempDescription))
        );
    }
}
