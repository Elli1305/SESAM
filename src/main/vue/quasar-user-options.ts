import "./styles/quasar.scss";
import iconSet from "quasar/icon-set/fontawesome-v5.js";
import "@quasar/extras/fontawesome-v5/fontawesome-v5.css";
import { Notify } from "quasar";

// Import QEditor component
import { QEditor } from "quasar";

// To be used on app.use(Quasar, { ... })
export default {
    config: {},
    components: {
        // Add QEditor to components
        QEditor,
    },
    plugins: {
        Notify,
    },
    iconSet: iconSet,
};
