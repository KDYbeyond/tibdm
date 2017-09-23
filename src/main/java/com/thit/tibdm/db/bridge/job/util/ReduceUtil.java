package com.thit.tibdm.db.bridge.job.util;

import com.alibaba.fastjson.JSON;
import com.thit.tibdm.NettyTransmission.util.ResourceUtil;
import com.thit.tibdm.api.TXIBDMApi;
import com.thit.tibdm.calculation.DBConnection;
import com.thit.tibdm.db.bridge.util.CassandraSingleConnect;
import com.thit.tibdm.db.bridge.util.CassandraV2Table;
import com.thit.tibdm.db.bridge.util.CassandraV2Util;
import com.thit.tibdm.imdb.bridge.util.RedisUtil;
import com.thit.tibdm.sparkstream.util.ProtocolUtil;
import com.thit.tibdm.util.ProtocolConstants;
import com.xicrm.common.TXISystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by dongzhiquan on 2017/6/22.
 */
public class ReduceUtil {

    public static final Logger logger = LoggerFactory.getLogger(ReduceUtil.class);

    private static String keyss = "[\"COLLECT_TIME\",\"ZT1\",\"ZT2\",\"ZT3\",\"ZT4\",\"ZT5\",\"ZT6\",\"ZT7\",\"ZT8\",\"ZT9\",\"ZT10\",\"ZT11\",\"ZT12\",\"ZT13\",\"ZT14\",\"ZT15\",\"ZT16\",\"ZT17\",\"ZT18\",\"ZT19\",\"ZT20\",\"ZT21\",\"ZT22\",\"ZT23\",\"ZT24\",\"ZT25\",\"ZT26\",\"ZT27\",\"ZT28\",\"ZT29\",\"ZT30\",\"ZT31\",\"ZT32\",\"ZT33\",\"ZT34\",\"ZT35\",\"ZT36\",\"ZT37\",\"ZT38\",\"ZT39\",\"ZT40\",\"ZT41\",\"ZT42\",\"ZT43\",\"ZT44\",\"ZT45\",\"ZT46\",\"ZT47\",\"ZT48\",\"ZT49\",\"ZT50\",\"ZT51\",\"ZT52\",\"ZT53\",\"ZT54\",\"ZT55\",\"ZT56\",\"ZT57\",\"ZT58\",\"ZT59\",\"ZT60\",\"ZT61\",\"ZT62\",\"ZT63\",\"ZT64\",\"ZT65\",\"ZT66\",\"ZT67\",\"ZT68\",\"ZT69\",\"ZT70\",\"ZT71\",\"ZT72\",\"ZT73\",\"ZT74\",\"ZT75\",\"ZT76\",\"ZT77\",\"ZT78\",\"ZT79\",\"ZT80\",\"ZT81\",\"ZT82\",\"ZT83\",\"ZT84\",\"ZT85\",\"ZT86\",\"ZT87\",\"ZT88\",\"ZT89\",\"ZT90\",\"ZT91\",\"ZT92\",\"ZT93\",\"ZT94\",\"ZT95\",\"ZT96\",\"ZT97\",\"ZT98\",\"ZT99\",\"ZT100\",\"ZT101\",\"ZT102\",\"ZT103\",\"ZT104\",\"ZT105\",\"ZT106\",\"ZT107\",\"ZT108\",\"ZT109\",\"ZT110\",\"ZT111\",\"ZT112\",\"ZT113\",\"ZT114\",\"ZT115\",\"ZT116\",\"ZT117\",\"ZT118\",\"ZT119\",\"ZT120\",\"ZT121\",\"ZT122\",\"ZT123\",\"ZT124\",\"ZT125\",\"ZT126\",\"ZT127\",\"ZT128\",\"ZT129\",\"ZT130\",\"ZT131\",\"ZT132\",\"ZT133\",\"ZT134\",\"ZT135\",\"ZT136\",\"ZT137\",\"ZT138\",\"ZT139\",\"ZT140\",\"ZT141\",\"ZT142\",\"ZT143\",\"ZT144\",\"ZT145\",\"ZT146\",\"ZT147\",\"ZT148\",\"ZT149\",\"ZT150\",\"ZT151\",\"ZT152\",\"ZT153\",\"ZT154\",\"ZT155\",\"ZT156\",\"ZT157\",\"ZT158\",\"ZT159\",\"ZT160\",\"ZT161\",\"ZT162\",\"ZT163\",\"ZT164\",\"ZT165\",\"ZT166\",\"ZT167\",\"ZT168\",\"ZT169\",\"ZT170\",\"ZT171\",\"ZT172\",\"ZT173\",\"ZT174\",\"ZT175\",\"ZT176\",\"ZT177\",\"ZT178\",\"ZT179\",\"ZT180\",\"ZT181\",\"ZT182\",\"ZT183\",\"ZT184\",\"ZT185\",\"ZT186\",\"ZT187\",\"ZT188\",\"ZT189\",\"ZT190\",\"ZT191\",\"ZT192\",\"ZT193\",\"ZT194\",\"ZT195\",\"ZT196\",\"ZT197\",\"ZT198\",\"ZT199\",\"ZT200\",\"ZT201\",\"ZT202\",\"ZT203\",\"ZT204\",\"ZT205\",\"ZT206\",\"ZT207\",\"ZT208\",\"ZT209\",\"ZT210\",\"ZT211\",\"ZT212\",\"ZT213\",\"ZT214\",\"ZT215\",\"ZT216\",\"ZT217\",\"ZT218\",\"ZT219\",\"ZT220\",\"ZT221\",\"ZT222\",\"ZT223\",\"ZT224\",\"ZT225\",\"ZT226\",\"ZT227\",\"ZT228\",\"ZT229\",\"ZT230\",\"ZT231\",\"ZT232\",\"ZT233\",\"ZT234\",\"ZT235\",\"ZT236\",\"ZT237\",\"ZT238\",\"ZT239\",\"ZT240\",\"ZT241\",\"ZT242\",\"ZT243\",\"ZT244\",\"ZT245\",\"ZT246\",\"ZT247\",\"ZT248\",\"ZT249\",\"ZT250\",\"ZT251\",\"ZT252\",\"ZT253\",\"ZT254\",\"ZT255\",\"ZT256\",\"ZT257\",\"ZT258\",\"ZT259\",\"ZT260\",\"ZT261\",\"ZT262\",\"ZT263\",\"ZT264\",\"ZT265\",\"ZT266\",\"ZT267\",\"ZT268\",\"ZT269\",\"ZT270\",\"ZT271\",\"ZT272\",\"ZT273\",\"ZT274\",\"ZT275\",\"ZT276\",\"ZT277\",\"ZT278\",\"ZT279\",\"ZT280\",\"ZT281\",\"ZT282\",\"ZT283\",\"ZT284\",\"ZT285\",\"ZT286\",\"ZT287\",\"ZT288\",\"ZT289\",\"ZT290\",\"ZT291\",\"ZT292\",\"ZT293\",\"ZT294\",\"ZT295\",\"ZT296\",\"ZT297\",\"ZT298\",\"ZT299\",\"ZT300\",\"ZT301\",\"ZT302\",\"ZT303\",\"ZT304\",\"ZT305\",\"ZT306\",\"ZT307\",\"ZT308\",\"ZT309\",\"ZT310\",\"ZT311\",\"ZT312\",\"ZT313\",\"ZT314\",\"ZT315\",\"ZT316\",\"ZT317\",\"ZT318\",\"ZT319\",\"ZT320\",\"ZT321\",\"ZT322\",\"ZT323\",\"ZT324\",\"ZT325\",\"ZT326\",\"ZT327\",\"ZT328\",\"ZT329\",\"ZT330\",\"ZT331\",\"ZT332\",\"ZT333\",\"ZT334\",\"ZT335\",\"ZT336\",\"ZT337\",\"ZT338\",\"ZT339\",\"ZT340\",\"ZT341\",\"ZT342\",\"ZT343\",\"ZT344\",\"ZT345\",\"ZT346\",\"ZT347\",\"ZT348\",\"ZT349\",\"ZT350\",\"ZT351\",\"ZT352\",\"ZT353\",\"ZT354\",\"ZT355\",\"ZT356\",\"ZT357\",\"ZT358\",\"ZT359\",\"ZT360\",\"ZT361\",\"ZT362\"" +
            ",\"ZT363\",\"ZT364\",\"ZT365\",\"ZT366\",\"ZT367\",\"ZT368\",\"ZT369\",\"ZT370\",\"ZT371\",\"ZT372\",\"ZT373\",\"ZT374\",\"ZT375\",\"ZT376\",\"ZT377\",\"ZT378\",\"ZT379\",\"ZT380\",\"ZT381\",\"ZT382\",\"ZT383\",\"ZT384\",\"ZT385\",\"ZT386\",\"ZT387\",\"ZT388\",\"ZT389\",\"ZT390\",\"ZT391\",\"ZT392\",\"ZT393\",\"ZT394\",\"ZT395\",\"ZT396\",\"ZT397\",\"ZT398\",\"ZT399\",\"ZT400\",\"ZT401\",\"ZT402\",\"ZT403\",\"ZT404\",\"ZT405\",\"ZT406\",\"ZT407\",\"ZT408\",\"ZT409\",\"ZT410\",\"ZT411\",\"ZT412\",\"ZT413\",\"ZT414\",\"ZT415\",\"ZT416\",\"ZT417\",\"ZT418\",\"ZT419\",\"ZT420\",\"ZT421\",\"ZT422\",\"ZT423\",\"ZT424\",\"ZT425\",\"ZT426\",\"ZT427\",\"ZT428\",\"ZT429\",\"ZT430\",\"ZT431\",\"ZT432\",\"ZT433\",\"ZT434\",\"ZT435\",\"ZT436\",\"ZT437\",\"ZT438\",\"ZT439\",\"ZT440\",\"ZT441\",\"ZT442\",\"ZT443\",\"ZT444\",\"ZT445\",\"ZT446\",\"ZT447\",\"ZT448\",\"ZT449\",\"ZT450\",\"ZT451\",\"ZT452\",\"ZT453\",\"ZT454\",\"ZT455\",\"ZT456\",\"ZT457\",\"ZT458\",\"ZT459\",\"ZT460\",\"ZT461\",\"ZT462\",\"ZT463\",\"ZT464\",\"ZT465\",\"ZT466\",\"ZT467\",\"ZT468\",\"ZT469\",\"ZT470\",\"ZT471\",\"ZT472\",\"ZT473\",\"ZT474\",\"ZT475\",\"ZT476\",\"ZT477\",\"ZT478\",\"ZT479\",\"ZT480\",\"ZT481\",\"ZT482\",\"ZT483\",\"ZT484\",\"ZT485\",\"ZT486\",\"ZT487\",\"ZT488\",\"ZT489\",\"ZT490\",\"ZT491\",\"ZT492\",\"ZT493\",\"ZT494\",\"ZT495\",\"ZT496\",\"ZT497\",\"ZT498\",\"ZT499\",\"ZT500\",\"ZT501\",\"ZT502\",\"ZT503\",\"ZT504\",\"ZT505\"" +
            ",\"ZT506\",\"ZT507\",\"ZT508\",\"ZT509\",\"ZT510\",\"ZT511\",\"ZT512\",\"ZT513\",\"ZT514\",\"ZT515\",\"ZT516\",\"ZT517\",\"ZT518\",\"ZT519\",\"ZT520\",\"ZT521\",\"ZT522\",\"ZT523\",\"ZT524\",\"ZT525\",\"ZT526\",\"ZT527\",\"ZT528\",\"ZT529\",\"ZT530\",\"ZT531\",\"ZT532\",\"ZT533\",\"ZT534\",\"ZT535\",\"ZT536\",\"ZT537\",\"ZT538\",\"ZT539\",\"ZT540\",\"ZT541\",\"ZT542\",\"ZT543\",\"ZT544\",\"ZT545\",\"ZT546\",\"ZT547\",\"ZT548\",\"ZT549\",\"ZT550\",\"ZT551\",\"ZT552\",\"ZT553\",\"ZT554\",\"ZT555\",\"ZT556\",\"ZT557\",\"ZT558\",\"ZT559\",\"ZT560\",\"ZT561\",\"ZT562\",\"ZT563\",\"ZT564\",\"ZT565\",\"ZT566\",\"ZT567\",\"ZT568\",\"ZT569\",\"ZT570\",\"ZT571\",\"ZT572\",\"ZT573\",\"ZT574\",\"ZT575\",\"ZT576\",\"ZT577\",\"ZT578\",\"ZT579\",\"ZT580\",\"ZT581\",\"ZT582\",\"ZT583\",\"ZT584\",\"ZT585\",\"ZT586\",\"ZT587\",\"ZT588\",\"ZT589\",\"ZT590\",\"ZT591\",\"ZT592\",\"ZT593\",\"ZT594\",\"ZT595\",\"ZT596\",\"ZT597\",\"ZT598\",\"ZT599\",\"ZT600\",\"ZT601\",\"ZT602\",\"ZT603\",\"ZT604\",\"ZT605\",\"ZT606\",\"ZT607\",\"ZT608\",\"ZT609\",\"ZT610\",\"ZT611\",\"ZT612\",\"ZT613\",\"ZT614\",\"ZT615\",\"ZT616\",\"ZT617\",\"ZT618\",\"ZT619\",\"ZT620\",\"ZT621\",\"ZT622\",\"ZT623\",\"ZT624\",\"ZT625\",\"ZT626\",\"ZT627\",\"ZT628\",\"ZT629\",\"ZT630\",\"ZT631\",\"ZT632\",\"ZT633\",\"ZT634\",\"ZT635\",\"ZT636\",\"ZT637\",\"ZT638\",\"ZT639\",\"ZT640\",\"ZT641\",\"ZT642\",\"ZT643\",\"ZT644\",\"ZT645\",\"ZT646\",\"ZT647\",\"ZT648\",\"ZT649\",\"ZT650\",\"ZT651\",\"ZT652\",\"ZT653\",\"ZT654\",\"ZT655\",\"ZT656\",\"ZT657\",\"ZT658\",\"ZT659\",\"ZT660\",\"ZT661\",\"ZT662\",\"ZT663\",\"ZT664\",\"ZT665\",\"ZT666\",\"ZT667\",\"ZT668\",\"ZT669\",\"ZT670\",\"ZT671\",\"ZT672\",\"ZT673\",\"ZT674\",\"ZT675\",\"ZT676\",\"ZT677\",\"ZT678\",\"ZT679\",\"ZT680\",\"ZT681\",\"ZT682\",\"ZT683\",\"ZT684\",\"ZT685\",\"ZT686\",\"ZT687\",\"ZT688\",\"ZT689\",\"ZT690\",\"ZT691\",\"ZT692\",\"ZT693\",\"ZT694\",\"ZT695\",\"ZT696\",\"ZT697\",\"ZT698\",\"ZT699\",\"ZT700\",\"ZT701\",\"ZT702\",\"ZT703\",\"ZT704\",\"ZT705\",\"ZT706\",\"ZT707\",\"ZT708\",\"ZT709\",\"ZT710\",\"ZT711\",\"ZT712\",\"ZT713\",\"ZT714\",\"ZT715\",\"ZT716\",\"ZT717\",\"ZT718\",\"ZT719\",\"ZT720\",\"ZT721\",\"ZT722\",\"ZT723\",\"ZT724\",\"ZT725\",\"ZT726\",\"ZT727\",\"ZT728\",\"ZT729\",\"ZT730\",\"ZT731\",\"ZT732\",\"ZT733\",\"ZT734\",\"ZT735\",\"ZT736\",\"ZT737\",\"ZT738\",\"ZT739\",\"ZT740\",\"ZT741\",\"ZT742\",\"ZT743\",\"ZT744\",\"ZT745\",\"ZT746\",\"ZT747\",\"ZT748\",\"ZT749\",\"ZT750\",\"ZT751\",\"ZT752\",\"ZT753\",\"ZT754\",\"ZT755\",\"ZT756\",\"ZT757\",\"ZT758\",\"ZT759\",\"ZT760\",\"ZT761\",\"ZT762\",\"ZT763\",\"ZT764\",\"ZT765\",\"ZT766\",\"ZT767\",\"ZT768\",\"ZT769\",\"ZT770\",\"ZT771\",\"ZT772\",\"ZT773\",\"ZT774\",\"ZT775\",\"ZT776\",\"ZT777\",\"ZT778\",\"ZT779\",\"ZT780\",\"ZT781\",\"ZT782\",\"ZT783\",\"ZT784\",\"ZT785\",\"ZT786\",\"ZT787\",\"ZT788\",\"ZT789\",\"ZT790\",\"ZT791\",\"ZT792\",\"ZT793\",\"ZT794\",\"ZT795\",\"ZT796\",\"ZT797\",\"ZT798\",\"ZT799\",\"ZT800\",\"ZT801\",\"ZT802\",\"ZT803\",\"ZT804\",\"ZT805\",\"ZT806\",\"ZT807\",\"ZT808\",\"ZT809\",\"ZT810\",\"ZT811\",\"ZT812\",\"ZT813\",\"ZT814\",\"ZT815\",\"ZT816\",\"ZT817\",\"ZT818\",\"ZT819\",\"ZT820\",\"ZT821\",\"ZT822\",\"ZT823\",\"ZT824\",\"ZT825\",\"ZT826\",\"ZT827\",\"ZT828\",\"ZT829\",\"ZT830\",\"ZT831\",\"ZT832\",\"ZT833\",\"ZT834\",\"ZT835\",\"ZT836\",\"ZT837\",\"ZT838\",\"ZT839\",\"ZT840\",\"ZT841\",\"ZT842\",\"ZT843\",\"ZT844\",\"ZT845\",\"ZT846\",\"ZT847\",\"ZT848\",\"ZT849\",\"ZT850\",\"ZT851\",\"ZT852\",\"ZT853\",\"ZT854\",\"ZT855\",\"ZT856\",\"ZT857\",\"ZT858\",\"ZT859\",\"ZT860\",\"ZT861\",\"ZT862\",\"ZT863\",\"ZT864\",\"ZT865\",\"ZT866\",\"ZT867\",\"ZT868\",\"ZT869\",\"ZT870\",\"ZT871\",\"ZT872\",\"ZT873\",\"ZT874\",\"ZT875\",\"ZT876\",\"ZT877\",\"ZT878\",\"ZT879\",\"ZT880\",\"ZT881\",\"ZT882\",\"ZT883\",\"ZT884\",\"ZT885\",\"ZT886\",\"ZT887\",\"ZT888\",\"ZT889\",\"ZT890\",\"ZT891\",\"ZT892\",\"ZT893\",\"ZT894\",\"ZT895\",\"ZT896\",\"ZT897\",\"ZT898\",\"ZT899\",\"ZT900\",\"ZT901\",\"ZT902\",\"ZT903\",\"ZT904\",\"ZT905\",\"ZT906\",\"ZT907\",\"ZT908\",\"ZT909\",\"ZT910\",\"ZT911\",\"ZT912\",\"ZT913\",\"ZT914\",\"ZT915\",\"ZT916\",\"ZT917\",\"ZT918\",\"ZT919\",\"ZT920\",\"ZT921\",\"ZT922\",\"ZT923\",\"ZT924\",\"ZT925\",\"ZT926\",\"ZT927\",\"ZT928\",\"ZT929\",\"ZT930\",\"ZT931\",\"ZT932\",\"ZT933\",\"ZT934\",\"ZT935\",\"ZT936\",\"ZT937\",\"ZT938\",\"ZT939\",\"ZT940\",\"ZT941\",\"ZT942\",\"ZT943\",\"ZT944\",\"ZT945\",\"ZT946\",\"ZT947\",\"ZT948\",\"ZT949\",\"ZT950\",\"ZT951\",\"ZT952\",\"ZT953\",\"ZT954\",\"ZT955\",\"ZT956\",\"ZT957\",\"ZT958\",\"ZT959\",\"ZT960\",\"ZT961\",\"ZT962\",\"ZT963\",\"ZT964\",\"ZT965\",\"ZT966\",\"ZT967\",\"ZT968\",\"ZT969\",\"ZT970\",\"ZT971\",\"ZT972\",\"ZT973\",\"ZT974\",\"ZT975\",\"ZT976\",\"ZT977\",\"ZT978\",\"ZT979\",\"ZT980\",\"ZT981\",\"ZT982\",\"ZT983\",\"ZT984\",\"ZT985\",\"ZT986\",\"ZT987\",\"ZT988\",\"ZT989\",\"ZT990\",\"ZT991\",\"ZT992\",\"ZT993\",\"ZT994\",\"ZT995\",\"ZT996\",\"ZT997\",\"ZT998\",\"ZT999\",\"ZT1000\",\"ZT1001\",\"ZT1002\",\"ZT1003\",\"ZT1004\",\"ZT1005\",\"ZT1006\",\"ZT1007\",\"ZT1008\",\"ZT1009\",\"ZT1010\",\"ZT1011\",\"ZT1012\",\"ZT1013\",\"ZT1014\",\"ZT1015\",\"ZT1016\",\"ZT1017\",\"ZT1018\",\"ZT1019\",\"ZT1020\",\"ZT1021\",\"ZT1022\",\"ZT1023\",\"ZT1024\",\"ZT1025\",\"ZT1026\",\"ZT1027\",\"ZT1028\",\"ZT1029\",\"ZT1030\",\"ZT1031\",\"ZT1032\",\"ZT1033\",\"ZT1034\",\"ZT1035\",\"ZT1036\",\"ZT1037\",\"ZT1038\",\"ZT1039\",\"ZT1040\",\"ZT1041\",\"ZT1042\",\"ZT1043\",\"ZT1044\",\"ZT1045\",\"ZT1046\",\"ZT1047\",\"ZT1048\",\"ZT1049\",\"ZT1050\",\"ZT1051\",\"ZT1052\",\"ZT1053\",\"ZT1054\",\"ZT1055\",\"ZT1056\",\"ZT1057\",\"ZT1058\",\"ZT1059\",\"ZT1060\",\"ZT1061\",\"ZT1062\",\"ZT1063\",\"ZT1064\",\"ZT1065\",\"ZT1066\",\"ZT1067\",\"ZT1068\",\"ZT1069\",\"ZT1070\",\"ZT1071\",\"ZT1072\",\"ZT1073\",\"ZT1074\",\"ZT1075\",\"ZT1076\",\"ZT1077\",\"ZT1078\",\"ZT1079\",\"ZT1080\",\"ZT1081\",\"ZT1082\",\"ZT1083\",\"ZT1084\",\"ZT1085\",\"ZT1086\",\"ZT1087\",\"ZT1088\",\"ZT1089\",\"ZT1090\",\"ZT1091\",\"ZT1092\",\"ZT1093\",\"ZT1094\",\"ZT1095\",\"ZT1096\",\"ZT1097\",\"ZT1098\",\"ZT1099\",\"ZT1100\",\"ZT1101\",\"ZT1102\",\"ZT1103\",\"ZT1104\",\"ZT1105\",\"ZT1106\",\"ZT1107\",\"ZT1108\",\"ZT1109\",\"ZT1110\",\"ZT1111\",\"ZT1112\",\"ZT1113\",\"ZT1114\",\"ZT1115\",\"ZT1116\",\"ZT1117\",\"ZT1118\",\"ZT1119\",\"ZT1120\",\"ZT1121\",\"ZT1122\",\"ZT1123\",\"ZT1124\",\"ZT1125\",\"ZT1126\",\"ZT1127\",\"ZT1128\",\"ZT1129\",\"ZT1130\",\"ZT1131\",\"ZT1132\",\"ZT1133\",\"ZT1134\",\"ZT1135\",\"ZT1136\",\"ZT1137\",\"ZT1138\",\"ZT1139\",\"ZT1140\",\"ZT1141\",\"ZT1142\",\"ZT1143\",\"ZT1144\",\"ZT1145\",\"ZT1146\",\"ZT1147\",\"ZT1148\",\"ZT1149\",\"ZT1150\",\"ZT1151\",\"ZT1152\",\"ZT1153\",\"ZT1154\",\"ZT1155\",\"ZT1156\",\"ZT1157\",\"ZT1158\",\"ZT1159\",\"ZT1160\",\"ZT1161\",\"ZT1162\",\"ZT1163\",\"ZT1164\",\"ZT1165\",\"ZT1166\",\"ZT1167\",\"ZT1168\",\"ZT1169\",\"ZT1170\",\"ZT1171\",\"ZT1172\",\"ZT1173\",\"ZT1174\",\"ZT1175\",\"ZT1176\",\"ZT1177\",\"ZT1178\",\"ZT1179\",\"ZT1180\",\"ZT1181\",\"ZT1182\",\"ZT1183\",\"ZT1184\",\"ZT1185\",\"ZT1186\",\"ZT1187\",\"ZT1188\",\"ZT1189\",\"ZT1190\",\"ZT1191\",\"ZT1192\",\"ZT1193\",\"ZT1194\",\"ZT1195\",\"ZT1196\",\"ZT1197\",\"ZT1198\",\"ZT1199\",\"ZT1200\",\"ZT1201\",\"ZT1202\",\"ZT1203\",\"ZT1204\",\"ZT1205\",\"ZT1206\",\"ZT1207\",\"ZT1208\",\"ZT1209\",\"ZT1210\",\"ZT1211\",\"ZT1212\",\"ZT1213\",\"ZT1214\",\"ZT1215\",\"ZT1216\",\"ZT1217\",\"ZT1218\",\"CY0\",\"CY1\",\"CY2\",\"CY3\"" +
            ",\"CY4\",\"CY5\",\"CY6\",\"CY7\",\"CY8\",\"CY9\",\"CY10\",\"CY11\",\"CY12\",\"CY13\",\"CY14\",\"CY15\",\"CY16\",\"CY17\",\"CY18\",\"CY19\",\"CY20\",\"CY21\",\"CY22\",\"CY23\",\"CY24\",\"CY25\",\"CY26\",\"CY27\",\"CY28\",\"CY29\",\"CY30\",\"CY31\",\"CY32\",\"CY33\",\"CY34\",\"CY35\",\"CY36\",\"CY37\",\"CY38\",\"CY39\",\"CY40\",\"CY41\",\"CY42\",\"CY43\",\"CY44\",\"CY45\",\"CY46\",\"CY47\"" +
            ",\"CY48\",\"CY49\",\"CY50\",\"CY51\",\"CY52\",\"CY53\",\"CY54\",\"CY55\",\"CY56\",\"CY57\",\"CY58\",\"CY59\",\"CY60\",\"CY61\",\"CY62\",\"CY63\",\"CY64\",\"CY65\",\"CY66\",\"CY67\",\"CY68\",\"CY69\",\"CY70\",\"CY71\",\"CY72\",\"CY73\",\"CY74\",\"CY75\",\"CY76\",\"CY77\",\"CY78\",\"CY79\",\"CY81\",\"CY82\",\"CY83\",\"CY84\",\"CY85\",\"CY86\",\"CY87\",\"CY88\",\"CY89\",\"CY90\",\"CY91\",\"CY92\",\"CY93\",\"CY94\",\"CY95\",\"CY96\",\"CY97\"" +
            ",\"CY98\",\"CY99\",\"CY80\"]";

    private static String keys = "";

    /**
     * 定时写入Cassandra
     *
     * @param cassTable  redis表
     * @param redisTable cass表
     * @return
     */
    public static void saveLineToCassandra(String redisTable, String cassTable) {

        Map<String, Map<String, String>> result = new HashMap<>();
        String lineJson = "";

//        String lineList = "[\"l01\",\"l02\",\"l03\",\"l04\",\"l05\",\"l06\",\"l07\",\"l08\",\"l09\",\"l10\",\"l11\",\"l12\",\"l13\",\"l14\",\"l15\",\"l16\",\"l17\"]";
        String lineList = JSON.toJSONString(getLineList());
        //根据协议中要统计的属性来改变
        keys = getKeys(cassTable);
//        logger.error(cassTable+"//"+keys);
        try {
            lineJson = TXIBDMApi.XiGetRealTimeByLineSum(redisTable, "240", lineList, keys, null);
            result = (Map<String, Map<String, String>>) JSON.parse(lineJson);

        } catch (Exception e) {
            e.printStackTrace();
        }
        long collect_time = 0;

        Map<String, String> value = new HashMap<>();
        String json = "";
        String key = "";
        for (Map.Entry<String, Map<String, String>> entry : result.entrySet()) {
            key = entry.getKey();//线路号
            value = entry.getValue();
            Map<String, Object> map = (Map) JSON.parseObject(value.toString());
            map.put(CassandraV2Table.machine_id, key);//把线路负值给machine id
            map.put(CassandraV2Table.collect_type, "240");
            collect_time = System.currentTimeMillis();
            map.put(CassandraV2Table.collect_time, collect_time);//collect_time转为long
            map.put(CassandraV2Table.collect_date, CassandraV2Util.getNowDate());

            json = JSON.toJSONString(map);
//            logger.error(cassTable+"//json==="+json);
            //存入Cassandra
            save2BigDB(cassTable, json);
        }

//        System.out.println("lineJson==" + json);
//        System.out.println("lo3=="+result.get("l03"));

    }

    /**
     * 定时写入Cassandra
     *
     * @param cassTable  cassTable
     * @param redisTable redisTable
     * @return
     */
    public static void saveMachineToCassandra(String redisTable, String cassTable) {

        keys = getKeys(cassTable);
//        logger.error(cassTable+"//"+keys);

        String machine_ids = "";
        //所有线路号
        List<String> list = getLineList();
        for (int i = 0; i < list.size(); i++) {
            List<String> ids =
                    DBConnection.getListBySql(ResourceUtil.getProValueByNameAndKey("cassandra-db", "ch_ids_sql")
                            .replace("linenum", list.get(i).toString()));
            machine_ids = JSON.toJSONString(ids);
            Map<String, Map<String, String>> mapAll = new HashMap<>();
            String str = "";
            try {
                str = TXIBDMApi.XiGetRealTimeDataByKey(redisTable, machine_ids, "240", keys, null);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            mapAll = (Map<String, Map<String, String>>) JSON.parse(str);

            mapAll.forEach((k, v) -> {
                String json = "";
                String value = JSON.toJSONString(v);
                Map<String, Object> map = (Map) JSON.parseObject(value);

                long collect_time = System.currentTimeMillis();
                map.put(CassandraV2Table.collect_time, collect_time);//collect_time转为long
                map.put(CassandraV2Table.machine_id, k);
                map.put(CassandraV2Table.collect_type, "240");
                map.put(CassandraV2Table.collect_date, CassandraV2Util.getNowDate());
                json = JSON.toJSONString(map);
//                logger.error(cassTable+"//json==="+json);
                //存入Cassandra
//                        System.out.println(json);
                save2BigDB(cassTable, json);
            });
        }


    }

    /**
     * @param table 表
     * @param json  json
     */
    public static void save2BigDB(String table, String json) {
        //json字符串转java对象
        String cql = "INSERT INTO " + table + " JSON '" + json + "';";

//        System.out.println(cql);
        CassandraSingleConnect.INSTANCE.getInstance().execute(cql);
    }

    /**
     * @param args 参数
     */
    public static void main(String[] args) {
        TXISystem.start();
        saveMachineToCassandra("shanghai_time", "tablemachie_time");
    }


    public static List<String> getLineList() {

        List<String> ids1 =
                DBConnection.getListBySql(ResourceUtil.getProValueByNameAndKey("cassandra-db", "lines_sql"));

        String id = ids1.get(0);
        String[] split = id.split(",");
        List<String> list1 = Arrays.asList(split);
        return list1;

    }

    public static String getKeys(String cassTable) {

        String keyRe = "";

        if (RedisUtil.hget(ProtocolConstants.REDUCE_TABLE, ProtocolConstants.REDUCE_KEYS) == null) {
            keyRe = ProtocolUtil.getReduceKeys(cassTable);
            RedisUtil.hset(ProtocolConstants.REDUCE_TABLE, ProtocolConstants.REDUCE_KEYS, keyRe);
        } else {
            keyRe = RedisUtil.hget(ProtocolConstants.REDUCE_TABLE, ProtocolConstants.REDUCE_KEYS);
        }

        return keyRe;
    }
}
