# GIT checkout:

git clone https://se.techfak.de/git/gp-se-ss-2023.door_poc.git

# general information

This POC will run a simulated door and an issuing self-service Application.
In order to use it with your cell phone wallet application, this service needs
to be exposed to the internet, and it has to be reachable via a DNS name.
[todo] add documentation on how to set up this for your own domain.
For ease of use, we prepared a cloud instance of this POC.
Each team will have their own endpoint running on a different port
and will have to use their own credentials to log in.
See team projects for credentials.

# wallet walk through

Firstly, you need to install the BC Wallet app:
https://www2.gov.bc.ca/gov/content/governments/government-id/bc-wallet
Next, head to the self issue service:
https://sesam.gp-se.de:3000/issue
And issue yourself the Alice T-Member credential.
Once you have this in your wallet, you can open the door:
https://sesam.gp-se.de:3000/
by scanning the QR Code and sending a proof.

# prepare

You need to have NPM installed and use node 18 to run this POC.
Additionally, you need to install the indy-sdk.

## install indy-sdk:

```
git clone https://github.com/hyperledger/indy-sdk.git
cd indy-sdk
cd libindy
# patch indy-sdk to support openssl3 (ubuntu>16)
patch -p2 < $GPSE_GIT_PATH/patch/indy-sdk-patch_ssl3.patch
sudo apt-get install -y build-essential pkg-config cmake libssl-dev libsqlite3-dev libzmq3-dev libncursesw5-dev libsodium-dev cargo
cargo build --features sodium_static
sudo install target/debug/libindy.so  /lib/x86_64-linux-gnu/
sudo mkdir /usr/include/indy
sudo install  include/* /usr/include/indy/
```

# How to run:

Prepare node18:

```
nvm install 18; nvm use 18 # or install node18 manually...
npm install --force 
```

Run:

```
npm run dev
```

App will run on http://localhost:3000

# Web-application

All endpoints of the web application will use HTTP BASIC AUTH for authentication.
You should have received credentials for your application instance.
The default/testing endpoint running on https://sesam.gp-se.de:3000 uses $LOGIN:$PASS as credentials.
The application handles two endpoints:

## Simulated door

This service is available via https://sesam.gp-se.de:3000/ and shows a simulated door and will present a proof request
as QR Code.
The default door is T.100, thus the website will redirect you to https://sesam.gp-se.de:3000/T.100

## Credential issuing self-service

A second service, accessible via https://sesam.gp-se.de:3000/issue, can be used to issue some dummy credentials.
You can select a credential type, use a pre-filled example credential or enter custom values, and click on the issue
button.
Use a wallet application on your phone to scan the QR code in order to receive your credential.

# API

In addition to the web application, this example will also provide some API endpoints that can be used to configure the
door and, additionally, to issue test credentials.
Please use the same authentication as for the web application (HTTP BASIC AUTH):

```
export LOGIN='gpse';
export PASS=<your pw>;
export API=https://$LOGIN:$PASS@sesam.gp-se.de:3000;
```

## Endpoint /api/proof/config

This endpoint returns all known door IDs as an JSON array.

### GET

```curl $API/api/proof/config```
Output:

```
[
    "T.100",
    "T.101"
]
```

## Endpoint /api/proof/config/$DOOR_ID

This is endpoint is used to configure the proof request that will be shown by the virtual door with ID $DOOR_ID.
The expected input is a JSON file, describing an object with two records, using the following supported syntax.
Please note that this config is using a subset of the attributes present in
the [IndyRequestProofFormat](https://github.com/hyperledger/aries-framework-javascript/blob/087980f1adf3ee0bc434ca9782243a62c6124444/packages/core/src/modules/proofs/formats/indy/IndyProofFormat.ts#L9)
definition.

```
ProofConfig:
{
  description: string,
  requestedAttributes: Record<string, ProofAttributeInfo>, 
  requestedPredicates: Record<string, ProofPredicateInfo>
}
```

The description field can be used to show a human-readable summary of the required credential values.
With ProofAttributeInfo being defined as an object with a (arbitrary chosen) name and an array of restrictions.
If the restriction array contains multiple entries, this attribute is required to be in at least one of them (OR).

```
ProofAttributeInfo:
{
    name: string;
    restrictions: AttributeFilter[];
}
``` 

The individual restrictions can be used to limit the requested attribute to be either equal to a specific value as
defined via attributeValue,
or restrict the acceptance to a specific credentialDefinitionId.

```
AttributeFilter:
{
  credentialDefinitionId?: string;
  attributeValue?: AttributeValue;
}
``` 

The credentialDefinitionId can be simple strings, referring to a specific credential definition ID on the ledger (e.g. "
RuGojwAn4arQgZfznN5R1U:3:CL:679209:T-MEMBER"), or one of the magic values listed below (e.g. $T-MEMBER).
The attributeValue has to comply to the following definition:

```
AttributeValue:
{
  name: string;
  value: string | number;
}
``` 

The name field has to refer to an entry from the credential definition (e.g. first_name) and the value refers to a
specific string or number value.
The second record, consisting of ProofPredicateInfo objects, that is used in the ProofConfig can be used to request the
attestation of specific predicates.
Similar to the ProofAttributeInfo, this object requires an (arbitrary) name and a list of restrictions.
In addition, this type of object also needs a predicateType (<,<=,>,>=) and a predicateValue (as number!).

```
ProofPredicateInfo:
{
    name: string;
    predicateType: ">" | ">=" | "<" | "<=";
    predicateValue: number;
    restrictions: AttributeFilter[];
}
``` 

The following "magic" keys can be used in a proof request configuration to allow for some flexibility:
| keyword | replacement value |
| ----------- | ----------- |
| $T-MEMBER | credDefId of T-Member |
| $T-TRAINING | credDefId of T-Training |
| $U-MEMBER | credDefId of U-Member |
| $U-TRAINING | credDefId of U-Training |
| $TODAY-YYYYMMDD | current date as YYYYMMDD |

### GET

In order to fetch the proof request configuration that is currently active for a given door id you can query the
endpoint using a http GET request with the door id in the uri. In the following example, we will query door T.100:
Example:
```curl $API/api/proof/config/T.100```
Output:

```
{
  "description" : "please present a non-expired t-member or u-member credential",
  "requestedPredicates" : {
    "expiration_date": {
      "name": "expiration_date",
      "predicateType": ">",
      "predicateValue": "$TODAY-YYYYMMDD",
      "restrictions": [
        { "credentialDefinitionId": "$T-MEMBER"},
        { "credentialDefinitionId": "$U-MEMBER"}
      ]
    }
  }
}
```

### POST

In order to configure a given door, you need to send a HTTP POST request to the endpoint. The door is identified by
$DOOR_ID. The example will set up door T.100.
Please make sure to use the content-type application/json.
Example: set the configuration to the provided JSON in ./config/door_tmember_or_umember.json
```curl -H 'Content-Type: application/json' -X POST -d @config/door_tmember_or_umember.json $API/api/proof/config/T.100```
Output: object with message ok or a descriptive error

```
{ error: "", message: "ok" }
```

### Examples

See the JSON files in config/ for some proof request examples.

## Endpoint /api/proof/request/$DOOR_ID

Normally you should not need to mess around with this endpoint as it is used internally in the virtual door application.

### GET

This endpoint is used by the virtual door to fetch a new proofRequest.
Example:
```curl $API/api/proof/request/T.100```
Output:

```
{
  "oobId": "80907ff3-ec9a-463b-81c0-67b0f5734ecb",
  "invitation": "https://sesam.gp-se.de:3003?oob=eyJAdHlwZSI6Imh0dHBzOi8vZGlkY29tbS5vcmcvb3V0LW9mLWJhbmQvMS4xL2ludml0YXRpb24iLCJAaWQiOiI5MzJjNmRhZS0zZjY2LTQ3NGYtYmI5MC05OGQyMDVkODQzYWEiLCJsYWJlbCI6IkdQLVNFIDIwMjMgPGRvb3I-IiwiYWNjZXB0IjpbImRpZGNvbW0vYWlwMSIsImRpZGNvbW0vYWlwMjtlbnY9cmZjMTkiXSwiaGFuZHNoYWtlX3Byb3RvY29scyI6WyJodHRwczovL2RpZGNvbW0ub3JnL2RpZGV4Y2hhbmdlLzEuMCIsImh0dHBzOi8vZGlkY29tbS5vcmcvY29ubmVjdGlvbnMvMS4wIl0sInNlcnZpY2VzIjpbeyJpZCI6IiNpbmxpbmUtMCIsInNlcnZpY2VFbmRwb2ludCI6Imh0dHBzOi8vc2VzYW0uZ3Atc2UuZGU6MzAwMyIsInR5cGUiOiJkaWQtY29tbXVuaWNhdGlvbiIsInJlY2lwaWVudEtleXMiOlsiZGlkOmtleTp6Nk1rZkd6bVpBeUNRWFBGN1dCQ01WcWQ0QzI1RU50SDlOa2laS2k2aEFzeUNtTDgiXSwicm91dGluZ0tleXMiOltdfV19",
  "error": "",
  "description": "please present a t-member or u-member credential in combination with a t-training"
}
```

### POST

Not supported.

## Endpoint /api/proof/result

Normally you should not need to mess around with this endpoint as it is used by the virtual door application.

### GET

Use this endpoint to query the state of a proof Request. Please pass oobId=<...> from a previous GET /api/proof/request
as a parameter.
Example:
```curl $API/api/proof/result?oobId=80907ff3-ec9a-463b-81c0-67b0f5734ecb```
Output:

```
connection pending
```

(output is subject to change to a JSON response in the future)

### POST

Not supported

## Endpoint /api/credential/issue

### GET

Unsupported.

### POST

This endpoint can be used to issue credentials as a self-service.
This is used by the web applications [credential self-service](#credential-issueing-self-service).
You can use this from your application in order to hand out credentials.
Please note that this is an unauthenticated endpoint because this is just a proof of concept.
Please do not use it in production.
This endpoint requires a set of values to be sent to the endpoint via the request body.
Here is an example for a membership credential:

```
{
  "agent": <"tlabs" | "university">,
  "credential": {
    "credentialDefinitionId": <the credential definition id, matching the agent>,
    "attributes": [
      {"name": "id",  "value": <num>},
      {"name": "first_name", "value": <string>},
      ...
      {"name": "expiration_date", "value": <date_yyyymmdd>}
    ]
  }
}
```

The credentialDefinitionId must be valid for this agent. It also allows using some magic
tags for auto replacement ($T-MEMBER, $U-MEMBER, $T-TRAINING, $U-TRAINING).
Example:
```curl -H 'Content-Type: text/plain' -X POST -d '{"agent":"tlabs","credential":{"credentialDefinitionId":"$T-MEMBER","attributes":[{"name":"id","value":"1"},{"name":"first_name","value":"Alice"},{"name":"last_name","value":"Ananas"},{"name":"birth_date","value":"19880101"},{"name":"expiration_date","value":"20241010"}]}}' $API/api/credential/issue```
Output:

```
https://sesam.gp-se.de:3001/_eeW1oK3koh9fee/?oob=eyJAdHlwZS......dfQ
```

This output represents a connection request and is internally linked to the credential definition. You can present this
string as a QR code. When scanned with the wallet application, the mobile phone will initiate an encrypted encryption
with the sesam.gp-se.de API and retrieve the credential.

# wallet store

wallet data is stored in /tmp/sgp_xxxx TODO: add more details

# docker on AWS

docker build . -t door
docker run --restart=always --name door --mount src="/home/ubuntu/tmp/sgp_33000",target=/tmp/sgp_33000,type=bind -p
33000-33009:33000-33009 door 